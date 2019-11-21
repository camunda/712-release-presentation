require([], function() {
  function createScriptTag(source) {
    var script = document.createElement('script');
    script.src = source;
    document.getElementsByTagName('head')[0].appendChild(script);
  }

  // Repeat for every script you want to load
  createScriptTag('/webjars/jquery/jquery.min.js');
  createScriptTag('/webjars/sockjs-client/sockjs.min.js');
  createScriptTag('/webjars/stomp-websocket/stomp.min.js');
});
