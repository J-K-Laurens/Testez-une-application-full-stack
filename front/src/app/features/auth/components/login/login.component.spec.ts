import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { Router } from '@angular/router';
import { of, throwError } from 'rxjs';
import { expect } from '@jest/globals';

import { SessionService } from 'src/app/services/session.service';
import { AuthService } from '../../services/auth.service';
import { LoginComponent } from './login.component';
import { SessionInformation } from 'src/app/interfaces/sessionInformation.interface';

describe('LoginComponent', () => {
  let component: LoginComponent;
  let fixture: ComponentFixture<LoginComponent>;
  let authService: AuthService;
  let sessionService: SessionService;
  let router: Router;

  const mockSessionInformation: SessionInformation = {
    token: 'test-token',
    type: 'Bearer',
    id: 1,
    username: 'test@test.com',
    firstName: 'John',
    lastName: 'Doe',
    admin: false
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [LoginComponent],
      providers: [
        SessionService,
        {
          provide: Router,
          useValue: { navigate: jest.fn() }
        }
      ],
      imports: [
        BrowserAnimationsModule,
        HttpClientModule,
        MatCardModule,
        MatIconModule,
        MatFormFieldModule,
        MatInputModule,
        ReactiveFormsModule
      ]
    })
      .compileComponents();
    
    fixture = TestBed.createComponent(LoginComponent);
    component = fixture.componentInstance;
    authService = TestBed.inject(AuthService);
    sessionService = TestBed.inject(SessionService);
    router = TestBed.inject(Router);
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should have an invalid form when empty', () => {
    expect(component.form.valid).toBeFalsy();
  });

  it('should login successfully', () => {
    jest.spyOn(authService, 'login').mockReturnValue(of(mockSessionInformation));
    jest.spyOn(sessionService, 'logIn');

    component.form.setValue({
      email: 'test@test.com',
      password: 'password123'
    });

    component.submit();

    expect(authService.login).toHaveBeenCalledWith({
      email: 'test@test.com',
      password: 'password123'
    });
    expect(sessionService.logIn).toHaveBeenCalledWith(mockSessionInformation);
    expect(router.navigate).toHaveBeenCalledWith(['/sessions']);
    expect(component.onError).toBeFalsy();
  });

  it('should handle login error', () => {
    jest.spyOn(authService, 'login').mockReturnValue(throwError(() => new Error('Login failed')));

    component.form.setValue({
      email: 'test@test.com',
      password: 'wrongpassword'
    });

    component.submit();

    expect(authService.login).toHaveBeenCalled();
    expect(component.onError).toBeTruthy();
  });
});
