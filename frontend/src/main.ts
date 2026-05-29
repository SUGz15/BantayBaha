import { bootstrapApplication } from '@angular/platform-browser';
import { RouteReuseStrategy, provideRouter, withPreloading, PreloadAllModules } from '@angular/router';
import { IonicRouteStrategy, provideIonicAngular } from '@ionic/angular/standalone';
import { provideHttpClient } from '@angular/common/http';
import { addIcons } from 'ionicons';
import { alertCircleOutline, filterOutline, locateOutline, refreshOutline, searchOutline } from 'ionicons/icons';

import { AppComponent } from './app/app.component';
import { routes } from './app/app.routes';

addIcons({
  alertCircleOutline,
  filterOutline,
  locateOutline,
  refreshOutline,
  searchOutline
});

bootstrapApplication(AppComponent, {
  providers: [
    provideIonicAngular(),
    provideRouter(routes, withPreloading(PreloadAllModules)),
    provideHttpClient(),
    { provide: RouteReuseStrategy, useClass: IonicRouteStrategy }
  ]
}).catch((error) => console.error(error));
