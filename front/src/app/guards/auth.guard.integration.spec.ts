import { TestBed } from '@angular/core/testing';
import { Router } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { Location } from '@angular/common';
import { Component } from '@angular/core';
import { expect } from '@jest/globals';

import { AuthGuard } from './auth.guard';
import { SessionService } from '../services/session.service';

@Component({ template: '' })
class DummyComponent {}

@Component({ template: 'Login Page' })
class LoginComponent {}

describe('AuthGuard Integration', () => {
  let guard: AuthGuard;
  let sessionService: SessionService;
  let router: Router;
  let location: Location;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [DummyComponent, LoginComponent],
      imports: [
        RouterTestingModule.withRoutes([
          { path: 'protected', component: DummyComponent, canActivate: [AuthGuard] },
          { path: 'login', component: LoginComponent }
        ])
      ],
      providers: [
        AuthGuard,
        SessionService
      ]
    }).compileComponents();

    guard = TestBed.inject(AuthGuard);
    sessionService = TestBed.inject(SessionService);
    router = TestBed.inject(Router);
    location = TestBed.inject(Location);
  });

  it('should allow navigation when user is logged in', async () => {
    sessionService.isLogged = true;

    await router.navigate(['/protected']);

    expect(location.path()).toBe('/protected');
  });

  it('should redirect to login when user is not logged in', async () => {
    sessionService.isLogged = false;

    await router.navigate(['/protected']);

    expect(location.path()).toBe('/login');
  });

  it('should allow multiple navigations with logged user', async () => {
    sessionService.isLogged = true;

    await router.navigate(['/protected']);
    expect(location.path()).toBe('/protected');

    await router.navigate(['/login']);
    expect(location.path()).toBe('/login');
  });
});

