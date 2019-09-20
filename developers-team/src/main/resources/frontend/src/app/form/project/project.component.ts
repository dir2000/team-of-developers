import { Component, OnInit } from '@angular/core';
import { NgForm } from '@angular/forms';
import { ProjectModel } from './../../model/project.model';
import {HttpClient, HttpHeaders, HttpParams} from '@angular/common/http';

@Component({
  selector: 'app-project',
  templateUrl: './project.component.html',
  styleUrls: ['./project.component.css']
})
export class ProjectComponent implements OnInit {

  constructor(private http: HttpClient) {

  }

  ngOnInit() {
  }
}
