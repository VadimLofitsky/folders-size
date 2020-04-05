function defineGlobalUtilsFunctions() {
    window.log = console.log.bind(console);

    window.$ = function (selector, context) {
        return (typeof context == 'undefined' ? document : context).querySelector(selector);
    };
    window.$$ = function (selector, context) {
        return (typeof context == 'undefined' ? document : context).querySelectorAll(selector);
    };

    NodeList.prototype.toArray = function () {
        return Array.prototype.slice.call(this);
    };
}