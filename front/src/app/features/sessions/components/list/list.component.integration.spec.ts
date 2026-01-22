import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import { expect } from '@jest/globals';

import { ListComponent } from './list.component';
import { SessionService } from 'src/app/services/session.service';
import { SessionApiService } from '../../services/session-api.service';
import { Session } from '../../interfaces/session.interface';

describe('ListComponent Integration', () => {
  let component: ListComponent;
  let fixture: ComponentFixture<ListComponent>;
  let httpMock: HttpTestingController;
  let sessionService: SessionService;

  const mockSessions: Session[] = [
    {
      id: 1,
      name: 'Yoga Session 1',
      description: 'Morning yoga',
      date: new Date('2024-01-15'),
      teacher_id: 1,
      users: [1, 2],
      createdAt: new Date(),
      updatedAt: new Date()
    },
    {
      id: 2,
      name: 'Yoga Session 2',
      description: 'Evening yoga',
      date: new Date('2024-01-16'),
      teacher_id: 2,
      users: [3],
      createdAt: new Date(),
      updatedAt: new Date()
    }
  ];

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ListComponent],
      imports: [
        HttpClientTestingModule,
        MatCardModule,
        MatIconModule
      ],
      providers: [
        SessionService,
        SessionApiService
      ]
    })
      .compileComponents();

    fixture = TestBed.createComponent(ListComponent);
    component = fixture.componentInstance;
    httpMock = TestBed.inject(HttpTestingController);
    sessionService = TestBed.inject(SessionService);

    sessionService.sessionInformation = {
      token: 'test-token',
      type: 'Bearer',
      id: 1,
      username: 'test@test.com',
      firstName: 'Test',
      lastName: 'User',
      admin: false
    };
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should load sessions list on init with real services', () => {
    fixture.detectChanges();

    const req = httpMock.expectOne('api/session');
    expect(req.request.method).toBe('GET');
    req.flush(mockSessions);

    expect(component.sessions$).toBeDefined();
  });

  it('should handle user admin status with real SessionService', () => {
    sessionService.sessionInformation!.admin = true;

    fixture.detectChanges();

    const req = httpMock.expectOne('api/session');
    req.flush(mockSessions);

    expect(component.user?.admin).toBe(true);
  });

  it('should handle non-admin user with real SessionService', () => {
    sessionService.sessionInformation!.admin = false;

    fixture.detectChanges();

    const req = httpMock.expectOne('api/session');
    req.flush(mockSessions);

    expect(component.user?.admin).toBe(false);
  });
});

