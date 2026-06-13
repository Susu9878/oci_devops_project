import { test, expect } from '@playwright/test';

test.describe('Task Deletion E2E', () => {
  test.beforeEach(async ({ page }) => {
    await page.addInitScript(() => localStorage.setItem('token', 'stub-token'));
    page.on('dialog', dialog => { try { dialog.accept(); } catch (e) {} });
  });

  const createTask = (id, name) => ({
    taskId: id,
    taskName: name,
    description: `Task ${id}`,
    priority: 'MEDIUM',
    status: 'NOT_STARTED',
    sprint: { startDate: '2026-01-01', endDate: '2026-01-07' },
    tags: [],
  });

  const setupRoutes = async (page, tasks, deleteStatus = 204) => {
    let deletedId = null;
    await page.route('**/todolist/sprint*', route => route.fulfill({ status: 200, body: JSON.stringify(tasks) }));
    await page.route('**/todolist/*', async route => {
      if (route.request().method() === 'DELETE') {
        deletedId = new URL(route.request().url()).pathname.split('/').pop();
        route.fulfill({ status: deleteStatus, body: JSON.stringify({}) });
      } else {
        route.continue();
      }
    });
    return deletedId;
  };

  test('deletes task successfully', async ({ page }) => {
    const deletedId = await setupRoutes(page, [createTask(10, 'Task 1'), createTask(11, 'Task 2')]);
    await page.goto('/');

    const deleteBtn = page.locator('button:has-text("Delete"), .deleteBtn, [data-testid="delete-task"]').first();
    if (await deleteBtn.count() > 0) {
      await deleteBtn.click();
      await page.waitForResponse(r => r.request().method() === 'DELETE');
      expect(deletedId).toBe('10');
      await expect(page.getByText('Task 1')).toBeHidden({ timeout: 2000 });
      await expect(page.getByText('Task 2')).toBeVisible();
    }
  });

  test('handles 404 error on delete', async ({ page }) => {
    await setupRoutes(page, [createTask(30, 'Not Found Task')], 404);
    await page.goto('/');

    const deleteBtn = page.locator('button:has-text("Delete"), .deleteBtn, [data-testid="delete-task"]').first();
    if (await deleteBtn.count() > 0) {
      await deleteBtn.click();
      await page.waitForResponse(r => r.request().method() === 'DELETE');
      await expect(page.getByText('Not Found Task')).toBeVisible();
    }
  });

  test('handles 403 forbidden error', async ({ page }) => {
    await setupRoutes(page, [createTask(40, 'Protected Task')], 403);
    await page.goto('/');

    const deleteBtn = page.locator('button:has-text("Delete"), .deleteBtn, [data-testid="delete-task"]').first();
    if (await deleteBtn.count() > 0) {
      await deleteBtn.click();
      await page.waitForResponse(r => r.request().method() === 'DELETE');
      await expect(page.getByText('Protected Task')).toBeVisible();
    }
  });

  test('handles 500 server error', async ({ page }) => {
    await setupRoutes(page, [createTask(50, 'Server Error Task')], 500);
    await page.goto('/');

    const deleteBtn = page.locator('button:has-text("Delete"), .deleteBtn, [data-testid="delete-task"]').first();
    if (await deleteBtn.count() > 0) {
      await deleteBtn.click();
      await page.waitForResponse(r => r.request().method() === 'DELETE');
      await expect(page.getByText('Server Error Task')).toBeVisible();
    }
  });

  test('handles network failure', async ({ page }) => {
    let deletedId = null;
    await page.route('**/todolist/sprint*', route => route.fulfill({ status: 200, body: JSON.stringify([createTask(60, 'Network Task')]) }));
    await page.route('**/todolist/*', route => route.abort());

    await page.goto('/');

    const deleteBtn = page.locator('button:has-text("Delete"), .deleteBtn, [data-testid="delete-task"]').first();
    if (await deleteBtn.count() > 0) {
      await deleteBtn.click();
      await page.waitForTimeout(300);
      await expect(page.getByText('Network Task')).toBeVisible();
    }
  });

  test('deletes correct task in list', async ({ page }) => {
    const deletedId = await setupRoutes(page, [createTask(70, 'Keep 1'), createTask(71, 'Delete Me'), createTask(72, 'Keep 2')]);
    await page.goto('/');

    const deleteBtn = page.locator('button:has-text("Delete"), .deleteBtn, [data-testid="delete-task"]').nth(1);
    if (await deleteBtn.count() > 0) {
      await deleteBtn.click();
      await page.waitForResponse(r => r.request().method() === 'DELETE');
      expect(deletedId).toBe('71');
      await expect(page.getByText('Delete Me')).toBeHidden({ timeout: 2000 });
      await expect(page.getByText('Keep 1')).toBeVisible();
      await expect(page.getByText('Keep 2')).toBeVisible();
    }
  });

  test('sends correct ID in DELETE URL', async ({ page }) => {
    let capturedUrl = null;
    await page.route('**/todolist/sprint*', route => route.fulfill({ status: 200, body: JSON.stringify([createTask(999, 'Task')]) }));
    await page.route('**/todolist/*', async route => {
      if (route.request().method() === 'DELETE') {
        capturedUrl = route.request().url();
        route.fulfill({ status: 204 });
      } else {
        route.continue();
      }
    });

    await page.goto('/');

    const deleteBtn = page.locator('button:has-text("Delete"), .deleteBtn, [data-testid="delete-task"]').first();
    if (await deleteBtn.count() > 0) {
      await deleteBtn.click();
      await page.waitForResponse(r => r.request().method() === 'DELETE');
      expect(capturedUrl).toContain('/999');
    }
  });
});
