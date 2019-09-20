import { Component } from '@angular/core';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'developers-team-front-end';
  public static get HOST_NAME(): string { return 'http://localhost:8080'; }
}
