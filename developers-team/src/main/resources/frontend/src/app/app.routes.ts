import {RouterModule, Routes} from '@angular/router';
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
import {NgModule} from '@angular/core';

const appRoutes: Routes = [
  {
    path: 'bills', component: BillComponent, children:
      [
        {path: 'create', component: CreateBillComponent},
        {path: 'update', component: CreateBillComponent},
        {path: 'query-result', component: CreateBillComponent},
        {path: 'show', component: ShowBillsComponent},
        {path: 'query', component: QueryBillsComponent}
      ]

  },
  {
    path: 'customers', component: CustomerComponent, children:
      [
        {path: 'create', component: CreateCustomerComponent},
        {path: 'update', component: CreateCustomerComponent},
        {path: 'query-result', component: CreateCustomerComponent},
        {path: 'query', component: QueryCustomerComponent},
        {path: 'show', component: ShowCustomersComponent}
      ]

  },
  {
    path: 'developers', component: DeveloperComponent, children:
      [
        {path: 'create', component: CreateDeveloperComponent},
        {path: 'update', component: CreateDeveloperComponent},
        {path: 'query-result', component: CreateDeveloperComponent},
        {path: 'query', component: QueryDeveloperComponent},
        {path: 'show', component: ShowDevelopersComponent}
      ]

  },
  {
    path: 'managers', component: ManagerComponent, children:
      [
        {path: 'create', component: CreateManagerComponent},
        {path: 'update', component: CreateManagerComponent},
        {path: 'query-result', component: CreateManagerComponent},
        {path: 'query', component: QueryManagerComponent},
        {path: 'show', component: ShowManagersComponent}
      ]

  },
  {
    path: 'projects', component: ProjectComponent, children:
      [
        {path: 'create', component: CreateProjectComponent},
        {path: 'update', component: CreateProjectComponent},
        {path: 'query-result', component: CreateProjectComponent},
        {path: 'query', component: QueryProjectComponent},
        {path: 'show', component: ShowProjectsComponent}
      ]

  },
  {
    path: 'tasks', component: TaskComponent, children:
      [
        {path: 'create', component: CreateTaskComponent},
        {path: 'update', component: CreateTaskComponent},
        {path: 'query-result', component: CreateTaskComponent},
        {path: 'query', component: QueryTaskComponent},
        {path: 'show', component: ShowTasksComponent}
      ]

  },
  {
    path: 'qualifications', component: QualificationComponent, children:
      [
        {path: 'create', component: CreateQualificationComponent},
        {path: 'update', component: CreateQualificationComponent},
        {path: 'query-result', component: CreateQualificationComponent},
        {path: 'query', component: QueryQualificationComponent},
        {path: 'show', component: ShowQualificationsComponent}
      ]

  },
  {
    path: 'works', component: WorkComponent, children:
      [
        {path: 'create', component: CreateWorkComponent},
        {path: 'update', component: CreateWorkComponent},
        {path: 'query-result', component: CreateWorkComponent},
        {path: 'query', component: QueryWorkComponent},
        {path: 'show', component: ShowWorksComponent}
      ]

  }
];

@NgModule({
  imports: [
    RouterModule.forRoot(
      appRoutes
    )
  ],
  exports: [RouterModule]
})
export class AppRoutes { }
