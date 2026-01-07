import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';

import { SessionApiService } from './session-api.service';
import { Session } from '../interfaces/session.interface';

describe('SessionsService', () => {
  let service: SessionApiService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        HttpClientTestingModule
      ]
    });
    service = TestBed.inject(SessionApiService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should retrieve all sessions', () => {
    const mockSessions: Session[] = [
      {
        id: 1,
        name: 'Yoga Session 1',
        description: 'Description 1',
        date: new Date(),
        teacher_id: 1,
        users: [1, 2],
        createdAt: new Date(),
        updatedAt: new Date()
      },
      {
        id: 2,
        name: 'Yoga Session 2',
        description: 'Description 2',
        date: new Date(),
        teacher_id: 2,
        users: [3],
        createdAt: new Date(),
        updatedAt: new Date()
      }
    ];

    service.all().subscribe((sessions) => {
      expect(sessions).toEqual(mockSessions);
      expect(sessions.length).toBe(2);
    });

    const req = httpMock.expectOne('api/session');
    expect(req.request.method).toBe('GET');
    req.flush(mockSessions);
  });

  it('should retrieve a session by id', () => {
    const mockSession: Session = {
      id: 1,
      name: 'Yoga Session 1',
      description: 'Description 1',
      date: new Date(),
      teacher_id: 1,
      users: [1, 2],
      createdAt: new Date(),
      updatedAt: new Date()
    };

    service.detail('1').subscribe((session) => {
      expect(session).toEqual(mockSession);
      expect(session.id).toBe(1);
    });

    const req = httpMock.expectOne('api/session/1');
    expect(req.request.method).toBe('GET');
    req.flush(mockSession);
  });

  it('should delete a session by id', () => {
    service.delete('1').subscribe((response) => {
      expect(response).toEqual({});
    });

    const req = httpMock.expectOne('api/session/1');
    expect(req.request.method).toBe('DELETE');
    req.flush({});
  });

  it('should create a new session', () => {
    const newSession: Session = {
      name: 'New Yoga Session',
      description: 'New Description',
      date: new Date(),
      teacher_id: 1,
      users: []
    };

    const createdSession: Session = {
      ...newSession,
      id: 1,
      createdAt: new Date(),
      updatedAt: new Date()
    };

    service.create(newSession).subscribe((session) => {
      expect(session).toEqual(createdSession);
      expect(session.id).toBe(1);
    });

    const req = httpMock.expectOne('api/session');
    expect(req.request.method).toBe('POST');
    expect(req.request.body).toEqual(newSession);
    req.flush(createdSession);
  });

  it('should update an existing session', () => {
    const updatedSession: Session = {
      id: 1,
      name: 'Updated Yoga Session',
      description: 'Updated Description',
      date: new Date(),
      teacher_id: 1,
      users: [1],
      createdAt: new Date(),
      updatedAt: new Date()
    };

    service.update('1', updatedSession).subscribe((session) => {
      expect(session).toEqual(updatedSession);
      expect(session.name).toBe('Updated Yoga Session');
    });

    const req = httpMock.expectOne('api/session/1');
    expect(req.request.method).toBe('PUT');
    expect(req.request.body).toEqual(updatedSession);
    req.flush(updatedSession);
  });

  it('should participate in a session', () => {
    service.participate('1', '2').subscribe();

    const req = httpMock.expectOne('api/session/1/participate/2');
    expect(req.request.method).toBe('POST');
    expect(req.request.body).toBeNull();
    req.flush(null);
  });

  it('should unparticipate from a session', () => {
    service.unParticipate('1', '2').subscribe();

    const req = httpMock.expectOne('api/session/1/participate/2');
    expect(req.request.method).toBe('DELETE');
    req.flush(null);
  });
});
