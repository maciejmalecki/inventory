import {Component} from '@angular/core';
import {Router} from '@angular/router';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {
  title = 'app';

  constructor(private readonly router: Router) {
  }

  goHome(): void {
    this.router.navigate(['/']).catch(reason => console.log(reason));
  }

  goItemClasses(): void {
    this.router.navigate(['/itemClasses']).catch(reason => console.log(reason));
  }

  goItems(): void {
    this.router.navigate(['/items']).catch(reason => console.log(reason));
  }
}
