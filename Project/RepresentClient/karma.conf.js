//jshint strict: false
module.exports = function(config) {
  config.set({

    basePath: './app',

    files: [
      'app/scripts/bower_components/angular/angular.js',
      'app/scripts/bower_components/angular-route/angular-route.js',
      'app/scripts/bower_components/angular-mocks/angular-mocks.js',
      'app/scripts/components/**/*.js',
      'view*/**/*.js'
    ],

    autoWatch: true,

    frameworks: ['jasmine'],

    browsers: ['Chrome'],

    plugins: [
      'karma-chrome-launcher',
      'karma-firefox-launcher',
      'karma-jasmine',
      'karma-junit-reporter'
    ],

    junitReporter: {
      outputFile: 'test_out/unit.xml',
      suite: 'unit'
    }

  });
};
