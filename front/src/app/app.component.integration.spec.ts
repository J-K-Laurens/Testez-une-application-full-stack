import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { MatToolbarModule } from '@angular/material/toolbar';
import { Router } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { expect } from '@jest/globals';

import { AppComponent } from './app.component';
import { SessionService } from './services/session.service';
import { SessionInformation } from './interfaces/sessionInformation.interface';

describe('AppComponent Integration', () => {
  let component: AppComponent;
  let fixture: ComponentFixture<AppComponent>;
  let sessionService: SessionService;
  let router: Router;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [AppComponent],
      imports: [
        HttpClientModule,
        MatToolbarModule,
        RouterTestingModule.withRoutes([
          { path: '', component: AppComponent }
        ])
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(AppComponent);
    component = fixture.componentInstance;
    sessionService = TestBed.inject(SessionService);
    router = TestBed.inject(Router);
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should expose $isLogged observable from SessionService', (done) => {
    const mockUser: SessionInformation = {
      token: 'test-token',
      type: 'Bearer',
      id: 1,
      username: 'test@test.com',
      firstName: 'John',
      lastName: 'Doe',
      admin: false
    };

    // Subscribe to the observable exposed by AppComponent
    component.$isLogged().subscribe(isLogged => {
      expect(isLogged).toBe(true);
      done();
    });

    // Trigger login to emit true
    sessionService.logIn(mockUser);
  });

  it('should logout and navigate to home page', async () => {
    // Setup: Log in first
    const mockUser: SessionInformation = {
      token: 'test-token',
      type: 'Bearer',
      id: 1,
      username: 'test@test.com',
      firstName: 'John',
      lastName: 'Doe',
      admin: false
    };
    sessionService.logIn(mockUser);
    expect(sessionService.isLogged).toBe(true);

    // Spy on router navigation
    const navigateSpy = jest.spyOn(router, 'navigate');

    // Execute logout
    component.logout();

    // Verify logout was called and navigation happened
    expect(sessionService.isLogged).toBe(false);
    expect(navigateSpy).toHaveBeenCalledWith(['']);
  });
});

