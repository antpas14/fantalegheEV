const gulp = require('gulp');
const tasks = require('gulp-task-listing');
const nodemon = require('gulp-nodemon');
const fs = require('fs');

gulp.task('nodemon', function (cb) {
	var started = false;
	return nodemon({
		script: 'webserver.js'
	}).on('start', function () {
		// to avoid nodemon being started multiple times
		// thanks @matthisk
		if (!started) {
			cb();
			started = true;
		}
	});
});
