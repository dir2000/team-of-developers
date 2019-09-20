import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';

import {AppComponent} from './app.component';
import {FormsModule} from '@angular/forms';
import {HttpClientModule} from '@angular/common/http';
import {AppRoutes} from './app.routes';
import {CustomerComponent} from './form/customer/customer.component';
import {CreateBillComponent} from './form/bill/create/create-bill.component';
import {ProjectComponent} from './form/project/project.component';
import {DeveloperComponent} from './form/developer/developer.component';
import {ManagerComponent} from './form/manager/manager.component';
import {ShowBillsComponent} from './form/bill/show/show-bills.component';
import {BillComponent} from './form/bill/bill.component';
import {CreateCustomerComponent} from './form/customer/create/create-customer.component';
import {ShowCustomersComponent} from './form/customer/show/show-customers.component';
import {CreateDeveloperComponent} from './form/developer/create-developer/create-developer.component';
import {ShowDevelopersComponent} from './form/developer/show-developers/show-developers.component';
import {CreateManagerComponent } from './form/manager/create-manager/create-manager.component';
import {ShowManagersComponent } from './form/manager/show-managers/show-managers.component';
import {CreateProjectComponent } from './form/project/create-project/create-project.component';
import {ShowProjectsComponent } from './form/project/show-projects/show-projects.component';
import {TaskComponent} from './form/task/task.component';
import {CreateTaskComponent} from './form/task/create/create-task.component';
import {ShowTasksComponent} from './form/task/show/show-tasks.component';
import { QualificationComponent } from './form/qualification/qualification.component';
import { CreateQualificationComponent } from './form/qualification/create-qualification/create-qualification.component';
import { ShowQualificationsComponent } from './form/qualification/show-qualifications/show-qualifications.component';
import { WorkComponent } from './form/work/work.component';
import { CreateWorkComponent } from './form/work/create-work/create-work.component';
import { ShowWorksComponent } from './form/work/show-works/show-works.component';
import { QueryBillsComponent } from './form/bill/query/query.component';
import { QueryCustomerComponent } from './form/customer/query/query.component';
import { QueryTaskComponent } from './form/task/query/query-task.component';
import { QueryDeveloperComponent } from './form/developer/query-developer/query-developer.component';
import { QueryManagerComponent } from './form/manager/query-manager/query-manager.component';
import { QueryProjectComponent } from './form/project/query-project/query-project.component';
import { QueryQualificationComponent } from './form/qualification/query-qualification/query-qualification.component';
import { QueryWorkComponent } from './form/work/query-work/query-work.component';

@NgModule({
  declarations: [
    AppComponent,
    CustomerComponent,
    CreateBillComponent,
    ProjectComponent,
    DeveloperComponent,
    ManagerComponent,
    ShowBillsComponent,
    BillComponent,
    CreateCustomerComponent,
    ShowCustomersComponent,
    CreateDeveloperComponent,
    ShowDevelopersComponent,
    CreateManagerComponent,
    ShowManagersComponent,
    CreateProjectComponent,
    ShowProjectsComponent,
    TaskComponent,
    CreateTaskComponent,
    ShowTasksComponent,
    QualificationComponent,
    CreateQualificationComponent,
    ShowQualificationsComponent,
    WorkComponent,
    CreateWorkComponent,
    ShowWorksComponent,
    QueryBillsComponent,
    QueryCustomerComponent,
    QueryTaskComponent,
    QueryDeveloperComponent,
    QueryManagerComponent,
    QueryProjectComponent,
    QueryQualificationComponent,
    QueryWorkComponent
  ],
  imports: [
    BrowserModule,
    FormsModule,
    AppRoutes,
    HttpClientModule,
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule {
}
