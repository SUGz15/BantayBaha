export type ProjectStatus = 'PLANNED' | 'ONGOING' | 'COMPLETED' | 'DELAYED' | 'DAMAGED' | 'UNKNOWN';

export interface ProjectMarker {
  id: number;
  title: string;
  status: ProjectStatus;
  latitude: number;
  longitude: number;
}

export interface ProjectDetail extends ProjectMarker {
  description: string;
  contractor: string;
  budget: number;
  startDate: string | null;
  targetCompletionDate: string | null;
}
