import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { map, Observable } from 'rxjs';

import { environment } from '../../../environments/environment';
import { ApiResponse } from '../models/api-response';
import { ProjectDetail, ProjectMarker, ProjectStatus } from '../models/project.model';

export interface ProjectQuery {
  search?: string;
  status?: ProjectStatus | 'ALL';
}

@Injectable({ providedIn: 'root' })
export class ProjectApiService {
  private readonly apiUrl = `${environment.apiBaseUrl}/projects`;

  constructor(private readonly http: HttpClient) {}

  getProjects(query: ProjectQuery = {}): Observable<ProjectMarker[]> {
    let params = new HttpParams();

    if (query.search?.trim()) {
      params = params.set('search', query.search.trim());
    }

    if (query.status && query.status !== 'ALL') {
      params = params.set('status', query.status);
    }

    return this.http
      .get<ApiResponse<ProjectMarker[]>>(this.apiUrl, { params })
      .pipe(map((response) => response.data));
  }

  getProject(id: number): Observable<ProjectDetail> {
    return this.http
      .get<ApiResponse<ProjectDetail>>(`${this.apiUrl}/${id}`)
      .pipe(map((response) => response.data));
  }
}
