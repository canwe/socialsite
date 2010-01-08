/*
 *     Copyright SocialSite (C) 2009
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
// Main Package of the application
var SocialSite = SocialSite || {};
// util package
SocialSite.Util = SocialSite.Util || {};

// FIXME send the converted(HTML) code to the server during the(need some more
// workaround)
// ajax form submit
SocialSite.Util.Editor = {
	// wmd options
	wmdOptions : {
		"preview" : true,
		"helpLink" : "http://daringfireball.net/projects/markdown/",
		"helpHoverTitle" : "Markdown Help"
	},
	// initiallizes the editor
	setUp : function() {
		var editor = $('textarea.richEditor:not(textarea.processed)');
		// setup up the editor
		editor.wmd(this.wmdOptions);
		editor.each(this.createEditor).val('').TextAreaResizer();
		SocialSite.Util.Slider.setUp();
		// creates the editor after each ajax response
		SocialSite.Ajax.registerPostAjax(this.PostAjaxSetUp);
	},

	// set up the editor after the ajax call
	PostAjaxSetUp : function(changed$) {
		var that = SocialSite.Util.Editor;
		var editor = changed$.find('textarea.richEditor');
		// setup the editor
		editor.wmd(that.wmdOptions);
		editor.each(that.createEditor).TextAreaResizer();
	},
	// adds hooks to the editor
	createEditor : function() {
		var textArea = $(this);
		var form = textArea.parents('form');
		// can't add a hook to the form submit due to the bug WICKET-1448
		// textArea.parents('form')[0].onsubmit = (function(){
		// var form = $(this);
		// console.log('hook called', form);
		// form.find('textarea.richEditor').val(form.find('div.wmd-priview').html());
		// return false;
		// });

		// FIXME this won't work if the user submit the form using the return
		// key
		form.find('a.editor-text').mousedown(function() {
			// set the textarea value to the output markup so that
				// the markup can be submitted to the server
				form.find('textarea.richEditor').val(
						form.find('div.wmd-preview').html());
				return false;
			}).click(function() {
			// clear the textarea after submitting the form
				// TODO find a clean way to do it
				form.find('textarea.richEditor').val("");
			});
	}
};

$().ready(function() {
	SocialSite.Util.Editor.setUp();
});
