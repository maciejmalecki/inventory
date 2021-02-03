import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {DashboardComponent} from './dashboard.component';
import {MatModule} from '../mat.module';
import {RouterModule} from '@angular/router';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';

@NgModule({
  declarations: [DashboardComponent],
  imports: [CommonModule, MatModule, RouterModule, ReactiveFormsModule, FormsModule],
  exports: [DashboardComponent]
})
export class DashboardModule {
}
