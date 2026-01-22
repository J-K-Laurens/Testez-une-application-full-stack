import { TestBed } from '@angular/core/testing';
import { Router } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { Location } from '@angular/common';
import { Component } from '@angular/core';
import { expect } from '@jest/globals';

import { UnauthGuard } from './unauth.guard';
import { SessionService } from '../services/session.service';

@Component({ template: 'Login Page' })
class LoginComponent {}

@Component({ template: 'Rentals Page' })
class RentalsComponent {}

describe('UnauthGuard Integration', () => {
  let guard: UnauthGuard;
  let sessionService: SessionService;
  let router: Router;
  let location: Location;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [LoginComponent, RentalsComponent],
      imports: [
        RouterTestingModule.withRoutes([
          { path: 'login', component: LoginComponent, canActivate: [UnauthGuard] },
          { path: 'rentals', component: RentalsComponent }
        ])
      ],
      providers: [
        UnauthGuard,
        SessionService
      ]
    }).compileComponents();

    guard = TestBed.inject(UnauthGuard);
    sessionService = TestBed.inject(SessionService);
    router = TestBed.inject(Router);
    location = TestBed.inject(Location);
  });

  it('should allow navigation to login when user is not logged in', async () => {
    sessionService.isLogged = false;

    await router.navigate(['/login']);

    expect(location.path()).toBe('/login');
  });

  it('should redirect to rentals when user is already logged in', async () => {
    sessionService.isLogged = true;

    await router.navigate(['/login']);

    expect(location.path()).toBe('/rentals');
  });
});

