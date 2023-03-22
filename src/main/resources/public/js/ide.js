let editor;

window.onload = function() {
	editor = ace.edit("editor");
	editor.setTheme("ace/theme/monokai");
	editor.session.setMode("ace/mode/c_cpp");
}

function changeLanguage() {

	let language = $("#languages").val();

	if (language == 'c' || language == 'cpp') editor.session.setMode("ace/mode/c_cpp");
	else if (language == 'php') editor.session.setMode("ace/mode/php");
	else if (language == 'python') editor.session.setMode("ace/mode/python");
	else if (language == 'node') editor.session.setMode("ace/mode/javascript");
	else if (language == 'java') {
		editor.session.setMode("ace/mode/java");
		editor.session.setValue("//Example code snippet\n\nclass Solution {\n\tpublic static void main(String[] args) {\n\t\tSystem.out.println(\"Hello, world!\");\n\t}\n}");
		editor.commands.addCommand({
			name: "beautify",
			bindKey: { win: "Ctrl-Shift-Alt-b", mac: "Command-Shift-B" },
			exec: function() {
				var editor = ace.edit("editor");
				var beautify1 = ace.require("ace/ext/beautify");
				ace.require("ace/ext/beautify").beautify(editor.session);
			}
		});
	};
}
$(document).ready(function() {
	const spinner = new Spinner({
			lines: 13,
			length: 40,
			width: 20,
			radius: 50,
			scale: 1,
			corners: 1,
			color: '#fff', // change color to white
			speed: 1,
			rotate: 0,
			animation: 'spinner-line-fade-quick',
			direction: 1,
			zIndex: 2e9,
			className: 'spinner',
			shadow: false,
			hwaccel: false,
			position: 'absolute'
		}).spin();

	function sendCode() {
		startSpinnerWheel();
		var encodedCode;
		var codeText = editor.getSession().getValue();
		encodedCode = btoa(codeText);
		$.ajax({
			url: "/compile",
			method: "POST",
			headers: {
				"Content-Type": "application/json",
				"language": $("#languages").val(),
				"code": encodedCode
			},
			success: function(response) {
				stopSpinnerWheel();
				const outputDiv = document.querySelector('.output');
				const ul = document.createElement('ul');
				response.forEach(item => {
					const li = document.createElement('li');
					li.textContent = item;
					ul.appendChild(li);
				});
				outputDiv.appendChild(ul);
			},
			error: function(xhr, status, error) {
				stopSpinnerWheel();
				console.error(error);
			}
		});
	}
	function beautify() {
		ace.require("ace/ext/beautify").beautify(editor.session);
	}
	function startSpinnerWheel() {
		const editorContainer = document.querySelector('.editor');
		// add spinner to editor container
		editorContainer.appendChild(spinner.el);
		var editor = ace.edit("editor");
		editor.setReadOnly(true);
	}
	function stopSpinnerWheel() {
		const editorContainer = document.querySelector('.editor');
		editorContainer.removeChild(spinner.el);
		var editor = ace.edit("editor");
		editor.setReadOnly(false);
	}
	$("#submit-btn").on("click", function() {
		var output = document.querySelector(".output");
		output.innerHTML = "";
		beautify();
		sendCode();

	});
	$("#beautify-btn").on("click", function() {
		beautify();
	});

});