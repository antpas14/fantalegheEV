const gulp = require('gulp');
const tasks = require('gulp-task-listing');
const nodemon = require('gulp-nodemon');
const fs = require('fs');

gulp.task('localhost', function (cb) {
	var started = false;
	return nodemon({
		script: 'webserver.js',
		args: ['localhost']
	}).on('start', function () {
		// to avoid nodemon being started multiple times
		// thanks @matthisk
		if (!started) {
			cb();
			started = true;
		}
	});
});

gulp.task('compose', function (cb) {
	var started = false;
	return nodemon({
		script: 'webserver.js',
		args: ['fantaleghe-backend']
	}).on('start', function () {
		// to avoid nodemon being started multiple times
		// thanks @matthisk
		if (!started) {
			cb();
			started = true;
		}
	});
});