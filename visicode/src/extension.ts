// The module 'vscode' contains the VS Code extensibility API
// Import the module and reference it with the alias vscode in your code below
import * as vscode from 'vscode'
const path = require('node:path')
const hljs = require('highlight.js') // https://highlightjs.org/

// This method is called when your extension is activated
// Your extension is activated the very first time the command is executed
export function activate(context: vscode.ExtensionContext) {
	context.subscriptions.push(
		vscode.commands.registerCommand('visicode-alpha.render', () => {
			// The code you place here will be executed every time your command is executed
			let activeEditor: vscode.TextEditor | undefined = vscode.window.activeTextEditor
			if (typeof activeEditor === 'undefined') {
				vscode.window.showErrorMessage('No active text editor.')
				return
			}
			const panel = vscode.window.createWebviewPanel(
				'webviewPanel',
				path.basename(activeEditor.document.fileName),
				vscode.ViewColumn.One,
				{}
			)
			panel.webview.html = getHTMLContent(context, activeEditor, panel)
		})
	)
}

function getHTMLContent(context: vscode.ExtensionContext, editor: vscode.TextEditor, panel: vscode.WebviewPanel): string {
	const cssPath = vscode.Uri.joinPath(context.extensionUri, 'highlight', 'styles', 'atom-one-dark.min.css')
	const scriptPath = vscode.Uri.joinPath(context.extensionUri, 'highlight', 'highlight.min.js')
	const cssUri = panel.webview.asWebviewUri(cssPath)
	const scriptUri = panel.webview.asWebviewUri(scriptPath)

	let code: string = editor.document.getText()
	let html = `<link rel="stylesheet" href="${cssUri}">\n<script src="${scriptUri}"></script>\n\n`
	
	let startCodePos = 0
	let startTagPos = 0
	let endTagPos = 0
	while (true) {
		startTagPos = code.indexOf("//!<visicode>", startTagPos)
		if (startTagPos == -1)
			break
		endTagPos = code.indexOf("//!</visicode>", endTagPos)
		if (endTagPos == -1) {
			vscode.window.showErrorMessage("Unmatched Tag!")
			return ""
		}

		let codeSnippet: string = code.substring(startCodePos, startTagPos)
		let taggedCodeSnippet: string = hljs.highlightAuto(codeSnippet).value
		html = 	html
				+ `<pre><code class="language-typescript" style="font-family: 'Courier New', Courier, monospace; font-size: 18px;">\n`
				+ taggedCodeSnippet
				+ `\n</code></pre>\n\n`

		let tag: string = code.substring(startTagPos + "//!<visicode>".length, endTagPos)
		let url = tag.substring(tag.indexOf("//") + 2)
		html = 	html + `<img src="${url}">\n\n`

		startCodePos = endTagPos + "//!</visicode>".length

		startTagPos++
		endTagPos++
	}

	let codeSnippet = code.substring(endTagPos == -1 ? 0 : endTagPos + "//!</visicode>".length)
	let taggedCodeSnippet: string = hljs.highlightAuto(codeSnippet).value
	html = 	html
				+ `<pre><code class="language-typescript" style="font-family: 'Courier New', Courier, monospace; font-size: 18px;">\n`
				+ taggedCodeSnippet
				+ `\n</code></pre>\n\n`	

	return html
}

// This method is called when your extension is deactivated
export function deactivate() {}
