import { test, expect } from '@playwright/test';

test.describe('Task Creation E2E', () => {
  test.beforeEach(async ({ page }) => {
    await page.addInitScript(() => localStorage.setItem('token', 'stub-token'));
    page.on('dialog', dialog => { try { dialog.accept(); } catch (e) {} });
  });

  const setupRoutes = async (page, postResponse = 201) => {
    await page.route('**/todolist/sprint*', route => route.fulfill({ status: 200, body: JSON.stringify([]) }));
    let capturedData = null;
    await page.route('http://localhost:8080/todolist', async route => {
      if (route.request().method() === 'POST') {
        capturedData = JSON.parse(await route.request().postData());
        route.fulfill({ status: postResponse, body: JSON.stringify({ taskId: 99, ...capturedData }) });
      } else {
        route.continue();
      }
    });
    return capturedData;
  };

  test('creates task with all fields', async ({ page }) => {
    const capturedData = await setupRoutes(page);
    await page.goto('/task');

    await page.getByPlaceholder('Task name').fill('Test Task');
    await page.getByPlaceholder('Description of task').fill('Test Desc');
    await page.getByPlaceholder('Story points').fill('8');
    await page.getByPlaceholder('Expected hours').fill('12');
    await page.locator('select[name="levPr"]').selectOption('HIGH');
    await page.locator('select[name="status"]').selectOption('IN_PROGRESS');
    await page.getByPlaceholder('UserId').fill('7');
    await page.getByPlaceholder('Sprint number').fill('2');

    await Promise.all([
      page.waitForResponse(r => r.request().method() === 'POST'),
      page.getByRole('button', { name: 'Create task' }).click(),
    ]);

    expect(capturedData).toMatchObject({
      taskName: 'Test Task',
      description: 'Test Desc',
      storyPoints: '8',
      expectedHours: '12',
      priority: 'HIGH',
      status: 'IN_PROGRESS',
      userId: '7',
      sprintId: '2',
    });

    await expect(page.getByPlaceholder('Task name')).toHaveValue('');
  });

  test('creates task with minimal fields', async ({ page }) => {
    const capturedData = await setupRoutes(page);
    await page.goto('/task');

    await page.getByPlaceholder('Task name').fill('Minimal');
    await page.getByPlaceholder('UserId').fill('1');
    await page.getByPlaceholder('Sprint number').fill('1');

    await Promise.all([
      page.waitForResponse(r => r.request().method() === 'POST'),
      page.getByRole('button', { name: 'Create task' }).click(),
    ]);

    expect(capturedData.taskName).toBe('Minimal');
  });

  test('handles backend validation error', async ({ page }) => {
    await page.route('**/todolist/sprint*', route => route.fulfill({ status: 200, body: JSON.stringify([]) }));
    await page.route('http://localhost:8080/todolist', route => 
      route.fulfill({ status: 400, body: JSON.stringify({ error: 'Invalid' }) })
    );

    await page.goto('/task');
    await page.getByPlaceholder('Task name').fill('Bad');
    await page.getByPlaceholder('UserId').fill('1');
    await page.getByPlaceholder('Sprint number').fill('1');
    await page.getByRole('button', { name: 'Create task' }).click();

    await page.waitForTimeout(300);
    await expect(page.getByPlaceholder('Task name')).toHaveValue('Bad');
  });

  test('handles network error', async ({ page }) => {
    await page.route('**/todolist/sprint*', route => route.fulfill({ status: 200, body: JSON.stringify([]) }));
    await page.route('http://localhost:8080/todolist', route => route.abort());

    await page.goto('/task');
    await page.getByPlaceholder('Task name').fill('Network');
    await page.getByPlaceholder('UserId').fill('1');
    await page.getByPlaceholder('Sprint number').fill('1');
    await page.getByRole('button', { name: 'Create task' }).click();

    await page.waitForTimeout(300);
    await expect(page.getByPlaceholder('Task name')).toHaveValue('Network');
  });

  test('tests all priority levels', async ({ page }) => {
    const capturedData = await setupRoutes(page);
    await page.goto('/task');

    for (const priority of ['LOWEST', 'LOW', 'MEDIUM', 'HIGH', 'CRITICAL']) {
      await page.getByPlaceholder('Task name').fill(`Task-${priority}`);
      await page.locator('select[name="levPr"]').selectOption(priority);
      await page.getByPlaceholder('UserId').fill('1');
      await page.getByPlaceholder('Sprint number').fill('1');

      await Promise.all([
        page.waitForResponse(r => r.request().method() === 'POST'),
        page.getByRole('button', { name: 'Create task' }).click(),
      ]);

      expect(capturedData.priority).toBe(priority);
      await page.waitForTimeout(100);
    }
  });
});
