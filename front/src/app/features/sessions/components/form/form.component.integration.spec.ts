import { TestBed, ComponentFixture } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { RouterTestingModule } from '@angular/router/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';
import { Router, ActivatedRoute } from '@angular/router';
import { expect } from '@jest/globals';

import { FormComponent } from './form.component';
import { SessionService } from '../../../../services/session.service';
import { SessionApiService } from '../../services/session-api.service';
import { TeacherService } from '../../../../services/teacher.service';
import { Session } from '../../interfaces/session.interface';
import { Teacher } from '../../../../interfaces/teacher.interface';

describe('FormComponent Integration', () => {
  let component: FormComponent;
  let fixture: ComponentFixture<FormComponent>;
  let httpMock: HttpTestingController;
  let sessionService: SessionService;
  let router: Router;

  const mockTeachers: Teacher[] = [
    {
      id: 1,
      lastName: 'Dupont',
      firstName: 'Marie',
      createdAt: new Date(),
      updatedAt: new Date()
    },
    {
      id: 2,
      lastName: 'Martin',
      firstName: 'Paul',
      createdAt: new Date(),
      updatedAt: new Date()
    }
  ];

  const mockSession: Session = {
    id: 1,
    name: 'Yoga Session',
    description: 'Test session',
    date: new Date('2026-01-15'),
    teacher_id: 1,
    users: [5],
    createdAt: new Date(),
    updatedAt: new Date()
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [FormComponent],
      imports: [
        HttpClientTestingModule,
        RouterTestingModule,
        ReactiveFormsModule,
        NoopAnimationsModule,
        MatSnackBarModule,
        MatCardModule,
        MatIconModule,
        MatFormFieldModule,
        MatInputModule,
        MatSelectModule
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
                get: jest.fn()
              }
            }
          }
        }
      ]
    }).compileComponents();

    sessionService = TestBed.inject(SessionService);
    httpMock = TestBed.inject(HttpTestingController);
    router = TestBed.inject(Router);

    // Mock admin user
    sessionService.sessionInformation = {
      token: 'test-token',
      type: 'Bearer',
      id: 5,
      username: 'admin@test.com',
      firstName: 'Admin',
      lastName: 'User',
      admin: true
    };
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should load teachers on create mode', () => {
    // Mock URL for create mode
    jest.spyOn(router, 'url', 'get').mockReturnValue('/sessions/create');

    fixture = TestBed.createComponent(FormComponent);
    component = fixture.componentInstance;

    fixture.detectChanges();

    // Expect teachers request
    const teachersReq = httpMock.expectOne('api/teacher');
    expect(teachersReq.request.method).toBe('GET');
    teachersReq.flush(mockTeachers);

    // Verify form is initialized
    expect(component.sessionForm).toBeDefined();
    expect(component.onUpdate).toBe(false);
  });

  it('should create a new session', () => {
    // Mock URL for create mode
    jest.spyOn(router, 'url', 'get').mockReturnValue('/sessions/create');

    fixture = TestBed.createComponent(FormComponent);
    component = fixture.componentInstance;

    fixture.detectChanges();

    // Flush teachers request
    httpMock.expectOne('api/teacher').flush(mockTeachers);

    // Fill form
    component.sessionForm!.setValue({
      name: 'New Yoga',
      date: '2026-02-01',
      teacher_id: 1,
      description: 'New session description'
    });

    // Submit
    component.submit();

    // Expect create request
    const createReq = httpMock.expectOne('api/session');
    expect(createReq.request.method).toBe('POST');
    expect(createReq.request.body).toEqual({
      name: 'New Yoga',
      date: '2026-02-01',
      teacher_id: 1,
      description: 'New session description'
    });
    createReq.flush(mockSession);
  });

  it('should load existing session in update mode', () => {
    // Mock URL for update mode
    jest.spyOn(router, 'url', 'get').mockReturnValue('/sessions/update/1');

    // Mock ActivatedRoute with session ID
    const activatedRoute = TestBed.inject(ActivatedRoute);
    jest.spyOn(activatedRoute.snapshot.paramMap, 'get').mockReturnValue('1');

    fixture = TestBed.createComponent(FormComponent);
    component = fixture.componentInstance;

    fixture.detectChanges();

    // Expect session detail request
    const sessionReq = httpMock.expectOne('api/session/1');
    expect(sessionReq.request.method).toBe('GET');
    sessionReq.flush(mockSession);

    // Verify update mode
    expect(component.onUpdate).toBe(true);
    expect(component.sessionForm?.value.name).toBe('Yoga Session');
  });

  it('should update an existing session', () => {
    // Mock URL for update mode
    jest.spyOn(router, 'url', 'get').mockReturnValue('/sessions/update/1');

    // Mock ActivatedRoute
    const activatedRoute = TestBed.inject(ActivatedRoute);
    jest.spyOn(activatedRoute.snapshot.paramMap, 'get').mockReturnValue('1');

    fixture = TestBed.createComponent(FormComponent);
    component = fixture.componentInstance;

    fixture.detectChanges();

    // Flush session request
    httpMock.expectOne('api/session/1').flush(mockSession);

    // Modify form
    component.sessionForm!.patchValue({
      name: 'Updated Yoga Session'
    });

    // Submit
    component.submit();

    // Expect update request
    const updateReq = httpMock.expectOne('api/session/1');
    expect(updateReq.request.method).toBe('PUT');
    expect(updateReq.request.body.name).toBe('Updated Yoga Session');
    updateReq.flush(mockSession);
  });

  it('should redirect non-admin users', () => {
    // Set user as non-admin
    sessionService.sessionInformation!.admin = false;

    // Mock URL
    jest.spyOn(router, 'url', 'get').mockReturnValue('/sessions/create');

    // Spy on navigation
    const navigateSpy = jest.spyOn(router, 'navigate');

    fixture = TestBed.createComponent(FormComponent);
    component = fixture.componentInstance;

    fixture.detectChanges();

    // Flush teachers request (still made even if redirected)
    httpMock.expectOne('api/teacher').flush(mockTeachers);

    // Verify redirect
    expect(navigateSpy).toHaveBeenCalledWith(['/sessions']);
  });
});

