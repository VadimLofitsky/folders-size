function defineGlobalUtilFunctions() {
    if (typeof log == 'undefined') window.log = console.log.bind(console);

    if (typeof $ == 'undefined') window.$ = function (selector, context) {
        return (typeof context == 'undefined' ? document : context).querySelector(selector);
    };
    if (typeof $$ == 'undefined') window.$$ = function (selector, context) {
        return (typeof context == 'undefined' ? document : context).querySelectorAll(selector);
    };

    NodeList.prototype.toArray = function () {
        return Array.prototype.slice.call(this);
    };
}