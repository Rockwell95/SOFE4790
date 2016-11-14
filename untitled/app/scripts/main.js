require.config({
    baseUrl: "scripts",
    paths: {
        'angular': 'bower_components/angular/angular.min',
        'angular-route': 'bower_components/angular-route/angular-route.min',
        'angularAMD': 'bower_components/angularAMD/angularAMD.min',
        'represent' : "node_modules/represent/lib/represent"
    },
    shim: { 'angularAMD': ['angular'], 'angular-route': ['angular'] },
    deps: ['app']
});