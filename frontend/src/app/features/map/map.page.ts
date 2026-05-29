import { AfterViewInit, Component, DestroyRef, ElementRef, OnInit, ViewChild, inject, signal } from '@angular/core';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { FormsModule } from '@angular/forms';
import {
  IonButton,
  IonButtons,
  IonContent,
  IonHeader,
  IonIcon,
  IonItem,
  IonLabel,
  IonList,
  IonSearchbar,
  IonSegment,
  IonSegmentButton,
  IonSpinner,
  IonTitle,
  IonToolbar,
  SegmentChangeEventDetail
} from '@ionic/angular/standalone';
import { IonSegmentCustomEvent, SearchbarInputEventDetail } from '@ionic/core';
import * as L from 'leaflet';

import { ProjectApiService } from '../../core/services/project-api.service';
import { ProjectMarker, ProjectStatus } from '../../core/models/project.model';

type StatusFilter = ProjectStatus | 'ALL';

@Component({
  selector: 'app-map',
  standalone: true,
  imports: [
    FormsModule,
    IonButton,
    IonButtons,
    IonContent,
    IonHeader,
    IonIcon,
    IonItem,
    IonLabel,
    IonList,
    IonSearchbar,
    IonSegment,
    IonSegmentButton,
    IonSpinner,
    IonTitle,
    IonToolbar
  ],
  templateUrl: './map.page.html',
  styleUrl: './map.page.scss'
})
export class MapPage implements OnInit, AfterViewInit {
  @ViewChild('mapContainer', { static: true })
  private readonly mapContainer!: ElementRef<HTMLDivElement>;

  readonly projects = signal<ProjectMarker[]>([]);
  readonly selectedProject = signal<ProjectMarker | null>(null);
  readonly loading = signal(false);
  readonly errorMessage = signal<string | null>(null);
  readonly statusFilter = signal<StatusFilter>('ALL');
  readonly searchTerm = signal('');

  private readonly destroyRef = inject(DestroyRef);
  private readonly projectApi = inject(ProjectApiService);
  private readonly markers = L.layerGroup();
  private map?: L.Map;

  ngOnInit(): void {
    this.loadProjects();
  }

  ngAfterViewInit(): void {
    this.map = L.map(this.mapContainer.nativeElement, {
      center: [14.5995, 120.9842],
      zoom: 11,
      zoomControl: false
    });

    L.control.zoom({ position: 'bottomright' }).addTo(this.map);

    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
      maxZoom: 19,
      attribution: '&copy; OpenStreetMap contributors'
    }).addTo(this.map);

    this.markers.addTo(this.map);
    setTimeout(() => this.map?.invalidateSize(), 0);
  }

  loadProjects(): void {
    this.loading.set(true);
    this.errorMessage.set(null);

    this.projectApi
      .getProjects({
        search: this.searchTerm(),
        status: this.statusFilter()
      })
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe({
        next: (projects) => {
          this.projects.set(projects);
          this.selectedProject.set(projects[0] ?? null);
          this.renderMarkers(projects);
          this.loading.set(false);
        },
        error: () => {
          this.errorMessage.set('Unable to load projects.');
          this.projects.set([]);
          this.markers.clearLayers();
          this.loading.set(false);
        }
      });
  }

  onSearch(event: CustomEvent<SearchbarInputEventDetail>): void {
    this.searchTerm.set(event.detail.value ?? '');
    this.loadProjects();
  }

  onStatusChange(event: IonSegmentCustomEvent<SegmentChangeEventDetail>): void {
    this.statusFilter.set((event.detail.value as StatusFilter | undefined) ?? 'ALL');
    this.loadProjects();
  }

  focusProject(project: ProjectMarker): void {
    this.selectedProject.set(project);
    this.map?.setView([project.latitude, project.longitude], 15, { animate: true });
  }

  refresh(): void {
    this.loadProjects();
  }

  private renderMarkers(projects: ProjectMarker[]): void {
    if (!this.map) {
      return;
    }

    this.markers.clearLayers();

    const bounds: L.LatLngTuple[] = [];

    for (const project of projects) {
      const position: L.LatLngTuple = [project.latitude, project.longitude];
      bounds.push(position);

      L.marker(position, {
        icon: this.markerIcon(project.status)
      })
        .bindPopup(`<strong>${project.title}</strong><br>${this.statusLabel(project.status)}`)
        .on('click', () => this.selectedProject.set(project))
        .addTo(this.markers);
    }

    if (bounds.length > 1) {
      this.map.fitBounds(bounds, { padding: [28, 28], maxZoom: 14 });
    } else if (bounds.length === 1) {
      this.map.setView(bounds[0], 14);
    }
  }

  private markerIcon(status: ProjectStatus): L.DivIcon {
    return L.divIcon({
      className: `project-marker project-marker-${status.toLowerCase()}`,
      html: '<span></span>',
      iconSize: [24, 24],
      iconAnchor: [12, 12]
    });
  }

  private statusLabel(status: ProjectStatus): string {
    return status.replace('_', ' ').toLowerCase();
  }
}
