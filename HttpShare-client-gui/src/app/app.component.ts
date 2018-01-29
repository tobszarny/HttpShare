import {Component, OnInit} from '@angular/core';
import {animate, keyframes, query, stagger, style, transition, trigger} from '@angular/animations';
import {FileRestService} from "./file.rest-service";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss'],
  animations: [
    trigger('files', [
      transition('* => *', [
        query(':enter', style({opacity: 0}), {optional: true}),

        query(':enter', stagger('300ms', [
          animate('.6s ease-in', keyframes([
            style({opacity: 0, transform: 'translateY(-75%)', offset: 0}),
            style({opacity: .5, transform: 'translateY(35px)', offset: .3}),
            style({opacity: 1, transform: 'translateY(0)', offset: 1})
          ]))]), {optional: true}),
        query(':leave', stagger('300ms', [
          animate('.6s ease-in', keyframes([
            style({opacity: 1, transform: 'translateY(0)', offset: 0}),
            style({opacity: .5, transform: 'translateY(35px)', offset: .3}),
            style({opacity: 0, transform: 'translateY(-75%)', offset: 1})
          ]))]), {optional: true}),
      ])
    ])
  ]
})
export class AppComponent implements OnInit {

  files: FileItem[] = [];

  constructor(private fileRestService: FileRestService) {

  }

  ngOnInit(): void {
    var that = this;
    that.files = that.fileRestService.getFiles();
    setInterval(() => {
      that.files = that.fileRestService.getFiles();
      console.log("File = " + that.files.length);
    }, 2000);
  }


}
