(function() {
  angular.module('JPIApp', [])
    .controller('AddressController', function($http, $scope, $attrs) {
      let ctrl = this;
      // It would be better to initialize an empty array,
      // and send this message from the server on connect.
      ctrl.messages = [
        { from: 'ChatBot', text: 'ようこそ。郵便番号を入力して下さい。'}
      ];

      let appendMessage = function(from, text) {
        ctrl.messages.push({ from: from, text: text });

      }

      ctrl.ws = new WebSocket($attrs.wsUrl);
      ctrl.ws.onmessage = function($event) {
        appendMessage('ChatBot', $event.data);
        $scope.$apply();  // http://jimhoskins.com/2012/12/17/angularjs-and-apply.html
      }

      ctrl.submit = function($event) {
        $event.preventDefault();
        ctrl.ws.send(ctrl.msg);
        appendMessage('User', ctrl.msg)
        ctrl.msg = '';
      };
    })
  // http://stackoverflow.com/questions/26343832/scroll-to-bottom-in-chat-box-in-angularjs
  .directive('ngScrollBottom', function() {
    return {
      scope: {
        ngScrollBottom: "="
      },
      link: function ($scope, $element) {
        $scope.$watchCollection('ngScrollBottom', function (newValue) {
          if (newValue) {
            $element[0].scrollTop = $element[0].scrollHeight;
          }
        });
      }
    }
  });
})();
