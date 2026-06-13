import { test, expect } from '@playwright/test';

test.describe('Task Filtering E2E', () => {
  const createTask = (id, name, importance = 'medium', tags = [], dateAdded = '2026-01-01', dateDue = '2026-01-15') => ({
    taskId: id,
    taskName: name,
    description: `Task ${name}`,
    priority: 'MEDIUM',
    status: 'NOT_STARTED',
    sprint: { startDate: '2026-01-01', endDate: '2026-01-07' },
    tags,
    importance,
    dateAdded,
    dateDue,
  });

  const setupRoutes = (page, tasks = []) => {
    page.route('**/todolist/sprint*', route => route.fulfill({ status: 200, body: JSON.stringify(tasks) }));
  };

  const openFilter = async (page) => {
    await page.locator('button:has-text("FILTER")').click();
  };

  test.beforeEach(async ({ page }) => {
    await page.addInitScript(() => {
      localStorage.setItem('token', 'mock-token');
      Object.defineProperty(Date, 'now', { value: () => new Date('2026-01-10').getTime() });
    });
  });

  test('filters by importance level', async ({ page }) => {
    for (const level of ['high', 'low', 'medium']) {
      const tasks = [
        createTask(1, 'High Task', 'high'),
        createTask(2, 'Low Task', 'low'),
        createTask(3, 'Medium Task', 'medium'),
      ];
      setupRoutes(page, tasks);
      await page.goto('/');

      await openFilter(page);
      const importanceSelect = page.locator('.filter-section').nth(1).locator('select');
      await importanceSelect.selectOption(level);

      await expect(page.getByText(`${level.charAt(0).toUpperCase() + level.slice(1)} Task`)).toBeVisible();
      const otherLevels = ['high', 'low', 'medium'].filter(l => l !== level);
      for (const other of otherLevels) {
        await expect(page.getByText(`${other.charAt(0).toUpperCase() + other.slice(1)} Task`)).not.toBeVisible();
      }
    }
  });

  test('filters by single tag', async ({ page }) => {
    const tasks = [
      createTask(1, 'Backend Task', 'medium', ['backend']),
      createTask(2, 'Frontend Task', 'medium', ['frontend']),
      createTask(3, 'API Task', 'medium', ['backend']),
    ];
    setupRoutes(page, tasks);
    await page.goto('/');

    await openFilter(page);
    const backendTag = page.locator('.filter-tag-button').filter({ hasText: /^backend$/ });
    await backendTag.click();

    await expect(page.getByText('Backend Task')).toBeVisible();
    await expect(page.getByText('API Task')).toBeVisible();
    await expect(page.getByText('Frontend Task')).not.toBeVisible();
  });

  test('filters by multiple tags', async ({ page }) => {
    const tasks = [
      createTask(1, 'Task A', 'medium', ['backend', 'urgent']),
      createTask(2, 'Task B', 'medium', ['backend']),
      createTask(3, 'Task C', 'medium', ['urgent']),
      createTask(4, 'Task D', 'medium', ['frontend']),
    ];
    setupRoutes(page, tasks);
    await page.goto('/');

    await openFilter(page);
    await page.locator('.filter-tag-button').filter({ hasText: /^backend$/ }).click();
    await page.locator('.filter-tag-button').filter({ hasText: /^urgent$/ }).click();

    for (const name of ['Task A', 'Task B', 'Task C']) {
      await expect(page.getByText(name)).toBeVisible();
    }
    await expect(page.getByText('Task D')).not.toBeVisible();
  });

  test('sorts tasks by date added (newest first)', async ({ page }) => {
    const tasks = [
      createTask(1, 'Oldest', 'medium', [], '2026-01-01'),
      createTask(2, 'Newest', 'medium', [], '2026-01-10'),
      createTask(3, 'Middle', 'medium', [], '2026-01-05'),
    ];
    setupRoutes(page, tasks);
    await page.goto('/');

    await openFilter(page);
    await page.locator('.filter-section').nth(0).locator('select').selectOption('added');

    const taskNames = await page.locator('.task-name').allTextContents();
    expect(taskNames[0]).toBe('Newest');
    expect(taskNames[2]).toBe('Oldest');
  });

  test('sorts tasks by due date (earliest first)', async ({ page }) => {
    const tasks = [
      createTask(1, 'Due Late', 'medium', [], '2026-01-01', '2026-01-20'),
      createTask(2, 'Due Soon', 'medium', [], '2026-01-01', '2026-01-12'),
    ];
    setupRoutes(page, tasks);
    await page.goto('/');

    await openFilter(page);
    await page.locator('.filter-section').nth(0).locator('select').selectOption('due');

    const taskNames = await page.locator('.task-name').allTextContents();
    expect(taskNames[0]).toBe('Due Soon');
  });

  test('combines importance and tag filters', async ({ page }) => {
    const tasks = [
      createTask(1, 'High Urgent', 'high', ['urgent']),
      createTask(2, 'High Normal', 'high', ['normal']),
      createTask(3, 'Medium Urgent', 'medium', ['urgent']),
    ];
    setupRoutes(page, tasks);
    await page.goto('/');

    await openFilter(page);
    await page.locator('.filter-section').nth(1).locator('select').selectOption('high');
    await page.locator('.filter-tag-button').filter({ hasText: /^urgent$/ }).click();

    await expect(page.getByText('High Urgent')).toBeVisible();
    await expect(page.getByText('High Normal')).not.toBeVisible();
    await expect(page.getByText('Medium Urgent')).not.toBeVisible();
  });

  test('clears all filters', async ({ page }) => {
    const tasks = [
      createTask(1, 'High', 'high', ['urgent']),
      createTask(2, 'Low', 'low'),
    ];
    setupRoutes(page, tasks);
    await page.goto('/');

    await openFilter(page);
    await page.locator('.filter-section').nth(1).locator('select').selectOption('high');
    await page.locator('.filter-tag-button').filter({ hasText: 'urgent' }).click();
    await expect(page.getByText('Low')).not.toBeVisible();

    await page.locator('button:has-text("Clear All")').click();
    await expect(page.getByText('High')).toBeVisible();
    await expect(page.getByText('Low')).toBeVisible();
  });

  test('toggles tag filter on and off', async ({ page }) => {
    const tasks = [
      createTask(1, 'Backend', 'medium', ['backend']),
      createTask(2, 'Frontend', 'medium', ['frontend']),
    ];
    setupRoutes(page, tasks);
    await page.goto('/');

    await openFilter(page);
    const tag = page.locator('.filter-tag-button').filter({ hasText: /^backend$/ });

    await tag.click();
    await expect(page.getByText('Frontend')).not.toBeVisible();

    await tag.click();
    await expect(page.getByText('Backend')).toBeVisible();
    await expect(page.getByText('Frontend')).toBeVisible();
  });

  test('displays correct tag states', async ({ page }) => {
    const tasks = [createTask(1, 'Task', 'medium', ['backend', 'frontend'])];
    setupRoutes(page, tasks);
    await page.goto('/');

    await openFilter(page);
    const backendTag = page.locator('.filter-tag-button').filter({ hasText: /^backend$/ });

    await expect(backendTag).toHaveClass(/inactive/);
    await backendTag.click();
    await expect(backendTag).toHaveClass(/active/);
  });

  test('closes filter on overlay click', async ({ page }) => {
    setupRoutes(page, [createTask(1, 'Task')]);
    await page.goto('/');

    await openFilter(page);
    await expect(page.locator('.filter-dropdown')).toBeVisible();

    await page.locator('.filter-dropdown-overlay').click();
    await expect(page.locator('.filter-dropdown')).not.toBeVisible();
  });

  test('handles no matching results', async ({ page }) => {
    setupRoutes(page, [createTask(1, 'High', 'high')]);
    await page.goto('/');

    await openFilter(page);
    await page.locator('.filter-section').nth(1).locator('select').selectOption('low');

    await expect(page.getByText('High')).not.toBeVisible();
  });

  test('handles tasks without tags', async ({ page }) => {
    const tasks = [
      createTask(1, 'Untagged', 'medium', []),
      createTask(2, 'Tagged', 'medium', ['backend']),
    ];
    setupRoutes(page, tasks);
    await page.goto('/');

    await openFilter(page);
    await page.locator('.filter-tag-button').filter({ hasText: /^backend$/ }).click();

    await expect(page.getByText('Tagged')).toBeVisible();
    await expect(page.getByText('Untagged')).not.toBeVisible();
  });
});
