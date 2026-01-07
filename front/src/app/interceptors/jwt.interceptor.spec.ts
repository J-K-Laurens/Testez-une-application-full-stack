import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { HTTP_INTERCEPTORS, HttpClient } from '@angular/common/http';
import { expect } from '@jest/globals';

import { JwtInterceptor } from './jwt.interceptor';
import { SessionService } from '../services/session.service';

describe('JwtInterceptor', () => {
  let httpMock: HttpTestingController;
  let httpClient: HttpClient;
  let sessionService: SessionService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [
        SessionService,
        {
          provide: HTTP_INTERCEPTORS,
          useClass: JwtInterceptor,
          multi: true
        }
      ]
    });

    httpMock = TestBed.inject(HttpTestingController);
    httpClient = TestBed.inject(HttpClient);
    sessionService = TestBed.inject(SessionService);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should be created', () => {
    const interceptor = TestBed.inject(JwtInterceptor);
    expect(interceptor).toBeTruthy();
  });

  it('should add Authorization header when user is logged in', () => {
    sessionService.isLogged = true;
    sessionService.sessionInformation = {
      token: 'test-token-123',
      type: 'Bearer',
      id: 1,
      username: 'test@test.com',
      firstName: 'Test',
      lastName: 'User',
      admin: false
    };

    httpClient.get('/api/test').subscribe();

    const req = httpMock.expectOne('/api/test');
    expect(req.request.headers.has('Authorization')).toBe(true);
    expect(req.request.headers.get('Authorization')).toBe('Bearer test-token-123');
    req.flush({});
  });

  it('should not add Authorization header when user is not logged in', () => {
    sessionService.isLogged = false;

    httpClient.get('/api/test').subscribe();

    const req = httpMock.expectOne('/api/test');
    expect(req.request.headers.has('Authorization')).toBe(false);
    req.flush({});
  });
});

