import { test, expect } from '@playwright/test';

class SignUpPage {
  constructor(page) {
    this.page = page;
    this.username = page.getByPlaceholder('Username');
    this.phoneNumber = page.getByPlaceholder('Phone number');
    this.email = page.getByPlaceholder('Email address');
    this.password = page.getByPlaceholder('Password');
    this.submit = page.getByRole('button', { name: 'Create Account' });
  }

  async goto() { await this.page.goto('/signup'); }
  async signup(username, phone, email, password) {
    await this.username.fill(username);
    await this.phoneNumber.fill(phone);
    await this.email.fill(email);
    await this.password.fill(password);
    await this.submit.click();
  }
}

const validUsers = [
  { name: 'standard', username: 'user123', phone: '+1-555-0123', email: 'user@example.com', password: 'Pass123!' },
  { name: 'minimal', username: 'u', phone: '123', email: 'a@b.com', password: 'p123' },
];

const invalidUsers = [
  { name: 'no-email', email: '', expectedError: 'email' },
  { name: 'invalid-email', email: 'not-email', expectedError: 'email' },
  { name: 'no-password', password: '', expectedError: 'password' },
];

test.describe('Signup (auth)', () => {
  test.beforeEach(async ({ page }) => {
    await page.addInitScript(() => {
      const fixed = 1669852800000;
      class DateOverride extends Date {
        constructor(...args) { return args.length === 0 ? super(fixed) : super(...args); }
        static now() { return fixed; }
      }
      window.Date = DateOverride;
    });
    page.on('dialog', dialog => { try { dialog.accept(); } catch (e) {} });
  });

  for (const user of validUsers) {
    test(`${user.name} - success`, async ({ page }) => {
      const signup = new SignUpPage(page);
      let request = null;

      await page.route('**/api/auth/register', async route => {
        request = route.request();
        route.fulfill({
          status: 200,
          body: JSON.stringify({ token: 'test-token', username: user.username, email: user.email }),
        });
      });

      await signup.goto();
      await signup.signup(user.username, user.phone, user.email, user.password);

      await expect(page).toHaveURL('/');
      const token = await page.evaluate(() => localStorage.getItem('token'));
      expect(token).toBe('test-token');

      const payload = JSON.parse(await request.postData());
      expect(payload).toMatchObject({
        username: user.username,
        email: user.email,
        password: user.password,
      });
    });
  }

  for (const user of invalidUsers) {
    test(`${user.name} - error`, async ({ page }) => {
      const signup = new SignUpPage(page);

      await page.route('**/api/auth/register', route =>
        route.fulfill({ status: 400, body: JSON.stringify({ error: `Invalid ${user.expectedError}` }) })
      );

      await signup.goto();
      await signup.signup('user', '+1-555', user.email || 'test@example.com', user.password || 'Pass123');
      await page.waitForTimeout(300);

      expect(page.url()).toContain('/signup');
      const token = await page.evaluate(() => localStorage.getItem('token'));
      expect(token).toBeNull();
    });
  }

  test('duplicate email - 409 conflict', async ({ page }) => {
    const signup = new SignUpPage(page);

    await page.route('**/api/auth/register', route =>
      route.fulfill({ status: 409, body: JSON.stringify({ error: 'Email already exists' }) })
    );

    await signup.goto();
    await signup.signup('user', '+1-555', 'existing@example.com', 'Pass123');
    await page.waitForTimeout(300);

    expect(page.url()).toContain('/signup');
    expect(await page.evaluate(() => localStorage.getItem('token'))).toBeNull();
  });

  test('network error', async ({ page }) => {
    const signup = new SignUpPage(page);
    await page.route('**/api/auth/register', route => route.abort());

    await signup.goto();
    await signup.signup('user', '+1-555', 'test@example.com', 'Pass123');
    await page.waitForTimeout(300);

    expect(page.url()).toContain('/signup');
    expect(await page.evaluate(() => localStorage.getItem('token'))).toBeNull();
  });

  test('server error 500', async ({ page }) => {
    const signup = new SignUpPage(page);

    await page.route('**/api/auth/register', route =>
      route.fulfill({ status: 500, body: JSON.stringify({ error: 'Internal error' }) })
    );

    await signup.goto();
    await signup.signup('user', '+1-555', 'test@example.com', 'Pass123');
    await page.waitForTimeout(300);

    expect(page.url()).toContain('/signup');
    expect(await page.evaluate(() => localStorage.getItem('token'))).toBeNull();
  });

  test('form data persists on error', async ({ page }) => {
    const signup = new SignUpPage(page);

    await page.route('**/api/auth/register', route =>
      route.fulfill({ status: 400, body: JSON.stringify({ error: 'Error' }) })
    );

    await signup.goto();
    await signup.signup('user123', '+1-555-0123', 'test@example.com', 'Pass123');
    await page.waitForTimeout(300);

    await expect(signup.username).toHaveValue('user123');
    await expect(signup.email).toHaveValue('test@example.com');
  });
});
