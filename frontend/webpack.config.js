var path = require('path');

console.log("**** ", path.resolve('.'))

module.exports = {
    resolve: {
        modules: [path.resolve('./node_modules'), path.resolve('./node_modules/@jetbrains')],
        /*alias: {
            "kotlin-react": "@jetbrains/kotlin-react",
            "kotlin-react-dom": "@jetbrains/kotlin-react-dom",
            "kotlin-extensions": "@jetbrains/kotlin-extensions"
        }*/
    }
};
