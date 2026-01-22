import { TestBed, ComponentFixture } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { RouterTestingModule } from '@angular/router/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { ActivatedRoute, Router } from '@angular/router';
import { of } from 'rxjs';
import { expect } from '@jest/globals';

import { DetailComponent } from './detail.component';
import { SessionService } from '../../../../services/session.service';
import { SessionApiService } from '../../services/session-api.service';
import { TeacherService } from '../../../../services/teacher.service';
import { Session } from '../../interfaces/session.interface';
import { Teacher } from '../../../../interfaces/teacher.interface';

describe('DetailComponent Integration', () => {
  let component: DetailComponent;
  let fixture: ComponentFixture<DetailComponent>;
  let httpMock: HttpTestingController;
  let sessionService: SessionService;
  let router: Router;

  const mockSession: Session = {
    id: 1,
    name: 'Yoga Session',
    description: 'Test session',
    date: new Date('2026-01-15'),
    teacher_id: 10,
    users: [5, 10],
    createdAt: new Date(),
    updatedAt: new Date()
  };

  const mockTeacher: Teacher = {
    id: 10,
    lastName: 'Dupont',
    firstName: 'Marie',
    createdAt: new Date(),
    updatedAt: new Date()
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [DetailComponent],
      imports: [
        HttpClientTestingModule,
        RouterTestingModule,
        ReactiveFormsModule,
        MatSnackBarModule,
        MatCardModule,
        MatIconModule,
        MatButtonModule
      ],
      providers: [
        SessionService,
        SessionApiService,
        TeacherService,
        {
          provide: ActivatedRoute,
          useValue: {
            snapshot: {
              paramMap: {
                get: () => '1' // Mock session ID
              }
            }
          }
        }
      ]
    }).compileComponents();

    sessionService = TestBed.inject(SessionService);
    httpMock = TestBed.inject(HttpTestingController);
    router = TestBed.inject(Router);

    // Mock logged user
    sessionService.sessionInformation = {
      token: 'test-token',
      type: 'Bearer',
      id: 5,
      username: 'test@test.com',
      firstName: 'Test',
      lastName: 'User',
      admin: false
    };

    fixture = TestBed.createComponent(DetailComponent);
    component = fixture.componentInstance;
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should load session and teacher details on init', () => {
    // Trigger ngOnInit
    fixture.detectChanges();

    // Expect session detail request
    const sessionReq = httpMock.expectOne('api/session/1');
    expect(sessionReq.request.method).toBe('GET');
    sessionReq.flush(mockSession);

    // Expect teacher detail request
    const teacherReq = httpMock.expectOne('api/teacher/10');
    expect(teacherReq.request.method).toBe('GET');
    teacherReq.flush(mockTeacher);

    // Verify component state
    expect(component.session).toEqual(mockSession);
    expect(component.teacher).toEqual(mockTeacher);
    expect(component.isParticipate).toBe(true); // User 5 is in users array
  });

  it('should participate in a session', () => {
    // Setup initial state
    fixture.detectChanges();

    // Flush initial session and teacher requests
    httpMock.expectOne('api/session/1').flush(mockSession);
    httpMock.expectOne('api/teacher/10').flush(mockTeacher);

    // Call participate
    component.participate();

    // Expect participate request
    const participateReq = httpMock.expectOne('api/session/1/participate/5');
    expect(participateReq.request.method).toBe('POST');
    participateReq.flush(null);

    // Expect fetchSession to be called again
    const refetchReq = httpMock.expectOne('api/session/1');
    expect(refetchReq.request.method).toBe('GET');
    refetchReq.flush(mockSession);

    const refetchTeacherReq = httpMock.expectOne('api/teacher/10');
    refetchTeacherReq.flush(mockTeacher);
  });

  it('should unparticipate from a session', () => {
    // Setup initial state
    fixture.detectChanges();

    // Flush initial requests
    httpMock.expectOne('api/session/1').flush(mockSession);
    httpMock.expectOne('api/teacher/10').flush(mockTeacher);

    // Call unParticipate
    component.unParticipate();

    // Expect unparticipate request
    const unparticipateReq = httpMock.expectOne('api/session/1/participate/5');
    expect(unparticipateReq.request.method).toBe('DELETE');
    unparticipateReq.flush(null);

    // Expect fetchSession to be called again
    const refetchReq = httpMock.expectOne('api/session/1');
    refetchReq.flush({...mockSession, users: [10]}); // User 5 removed

    const refetchTeacherReq = httpMock.expectOne('api/teacher/10');
    refetchTeacherReq.flush(mockTeacher);

    expect(component.isParticipate).toBe(false);
  });

  it('should call window.history.back() when back is called', () => {
    // Setup initial state
    fixture.detectChanges();
    httpMock.expectOne('api/session/1').flush(mockSession);
    httpMock.expectOne('api/teacher/10').flush(mockTeacher);

    // Spy on window.history.back
    const backSpy = jest.spyOn(window.history, 'back').mockImplementation();

    // Call back
    component.back();

    // Verify
    expect(backSpy).toHaveBeenCalled();

    // Cleanup
    backSpy.mockRestore();
  });

  it('should send delete request when delete is called', () => {
    // Setup initial state with admin user
    sessionService.sessionInformation = {
      token: 'test-token',
      type: 'Bearer',
      id: 5,
      username: 'admin@test.com',
      firstName: 'Admin',
      lastName: 'User',
      admin: true
    };

    fixture.detectChanges();
    httpMock.expectOne('api/session/1').flush(mockSession);
    httpMock.expectOne('api/teacher/10').flush(mockTeacher);

    // Call delete
    component.delete();

    // Expect delete request
    const deleteReq = httpMock.expectOne('api/session/1');
    expect(deleteReq.request.method).toBe('DELETE');
    deleteReq.flush(null);

  });

});

