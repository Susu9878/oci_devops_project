import { test, expect } from '@playwright/test';

class LoginPage {
  /**
   * @param {import('@playwright/test').Page} page
   */
  constructor(page) {
    this.page = page;
    this.email = page.getByPlaceholder('Email');
    this.password = page.getByPlaceholder('Password');
    this.submit = page.getByRole('button', { name: 'Login' });
  }
  async goto() { await this.page.goto('/login'); }
  async login(email, pass) {
    await this.email.fill(email);
    await this.password.fill(pass);
    await this.submit.click();
  }
}

const users = [
  { name: 'valid', user: 'test@example.com', pass: 'password123' },
  { name: 'invalid', user: 'test@example.com', pass: 'wrongpass' },
];

test.describe('Login flow (auth)', () => {
  let context;
  test.beforeAll(async ({ browser }) => {
    context = await browser.newContext();
    await context.addInitScript(() => {
      const fixed = 1669852800000;
      class DateOverride extends Date {
        constructor(...args) {
          if (args.length === 0) return super(fixed);
          return super(...args);
        }
        static now() { return fixed; }
      }
      window.Date = DateOverride;
    });
  });

  test.afterAll(async () => {
    await context.close();
  });

  test.beforeEach(async ({}, testInfo) => {
  });

  test.afterEach(async ({}, testInfo) => {
  });

  for (const u of users) {
    test(`${u.name} - login`, async ({ browser }, testInfo) => {
      testInfo.annotations.push({ type: 'tag', description: u.name === 'valid' ? 'smoke' : 'negative' });

      const page = await context.newPage();
      const login = new LoginPage(page);

      await context.route('**/api/auth/login', route => {
        if (u.name === 'valid') {
          route.fulfill({
            status: 200,
            contentType: 'application/json',
            body: JSON.stringify({ token: 'stub-token-123' }),
          });
        } else {
          route.fulfill({
            status: 401,
            contentType: 'application/json',
            body: JSON.stringify({ message: 'Invalid email or password' }),
          });
        }
      });

      await login.goto();

      await login.login(u.user, u.pass);

      if (u.name === 'valid') {
        await expect(page).toHaveURL('http://localhost:3000/');
        await expect(page.evaluate(() => localStorage.getItem('token'))).resolves.toBe('stub-token-123');
        await expect(page).toHaveScreenshot({ name: `welcome-${u.name}.png` });
      } else {
        await page.waitForTimeout(500);
        await expect(page).toHaveURL('http://localhost:3000/login');
        await expect(page.getByRole('button', { name: 'Login' })).toBeVisible();
      }

      await page.close();
    });
  }

  test('known flaky login', async ({ page }) => {
    test.skip(true, 'Skipping flaky test until bug fixed');
    test.slow();
  });

  test('expected to fail', async ({ page }) => {
    test.fail(true);
    await page.goto('/login');
    await expect(page).toHaveTitle('Non-existent title');
  });
});