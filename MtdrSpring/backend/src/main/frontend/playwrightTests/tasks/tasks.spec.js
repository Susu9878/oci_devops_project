import { test, expect } from '@playwright/test';

test.describe('Task CRUD (E2E)', () => {
  test.beforeEach(async ({ page }) => {
    await page.addInitScript(() => {
      localStorage.setItem('token', 'stub-token-123');
    });
    page.on('dialog', async dialog => { try { await dialog.accept(); } catch (e) {} });
  });

  test('reads tasks from API and displays them', async ({ page }) => {
    const mockTasks = [
      {
        taskId: 1,
        taskName: 'Initial Task',
        description: 'Initial description',
        priority: 'MEDIUM',
        status: 'NOT_STARTED',
        sprint: { startDate: '2026-01-01', endDate: '2026-01-07' },
        tags: ['tag1'],
      },
    ];

    await page.route('**/todolist/sprint*', route =>
      route.fulfill({ status: 200, contentType: 'application/json', body: JSON.stringify(mockTasks) })
    );

    await page.goto('/');

    await expect(page.getByText('Initial Task')).toBeVisible();
    await expect(page.getByText('Initial description')).toBeHidden();
    await page.locator('.task-header-content').first().click();
    await expect(page.getByText('Initial description')).toBeVisible();
  });

  test('creates a new task via CreateTask form', async ({ page }) => {
    await page.route('**/todolist/sprint*', route => route.fulfill({ status: 200, contentType: 'application/json', body: JSON.stringify([]) }));

    let postRequest = null;
    await page.route('**/todolist', async route => {
      const request = route.request();
      if (request.method() === 'POST') {
        postRequest = request;
        const body = await request.postData();
        const created = { taskId: 42, ...JSON.parse(body) };
        await route.fulfill({ status: 201, contentType: 'application/json', body: JSON.stringify(created) });
      } else {
        await route.continue();
      }
    });

    await page.goto('/task');

    await page.getByPlaceholder('Task name').fill('E2E Task');
    await page.getByPlaceholder('Description of task').fill('E2E Description');
    await page.getByPlaceholder('Story points').fill('3');
    await page.getByPlaceholder('Expected hours').fill('5');
    await page.locator('select[name="levPr"]').selectOption('MEDIUM');
    await page.locator('select[name="status"]').selectOption('NOT_STARTED');
    await page.getByPlaceholder('UserId').fill('1');
    await page.getByPlaceholder('Sprint number').fill('1');

    await Promise.all([
      page.waitForResponse(r => r.request().method() === 'POST' && /todolist/.test(r.url())),
      page.getByRole('button', { name: 'Create task' }).click(),
    ]);

    expect(postRequest).not.toBeNull();
    const payload = JSON.parse(await postRequest.postData());
    expect(payload.taskName).toBe('E2E Task');
    expect(payload.description).toBe('E2E Description');

    await expect(page.getByPlaceholder('Task name')).toHaveValue('');
  });

  test('updates a task via Modify modal', async ({ page }) => {
    const existing = [
      {
        taskId: 5,
        taskName: 'To update',
        description: 'Old desc',
        priority: 'LOW',
        status: 'NOT_STARTED',
        sprint: { startDate: '2026-01-01', endDate: '2026-01-07' },
        tags: [],
      },
    ];

    await page.route('**/todolist/sprint*', route => route.fulfill({ status: 200, contentType: 'application/json', body: JSON.stringify(existing) }));

    await page.route('**/todolist/*', async route => {
      const req = route.request();
      if (req.method() === 'PUT') {
        const body = await req.postData();
        const updated = JSON.parse(body);
        updated.taskId = 5;
        await route.fulfill({ status: 200, contentType: 'application/json', body: JSON.stringify(updated) });
      } else {
        await route.continue();
      }
    });

    await page.goto('/');
    await page.locator('.task-header-content').first().click();
    await page.locator('.editBtn').first().click();

    await page.locator('.modifyInput').fill('Updated Name');
    await page.locator('.txtArea').fill('Updated description');

    await Promise.all([
      page.waitForResponse(r => r.request().method() === 'PUT' && /todolist/.test(r.url())),
      page.getByRole('button', { name: 'Save Changes' }).click(),
    ]);

    await expect(page.getByText('Updated Name')).toBeVisible();
  });
});
