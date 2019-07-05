'use strict';
(function (document, window, index) {
    //used for thymeleaf
    /*<![CDATA[*/
    var maxUploadB = /*[[${maxUploadB}]]*/ 1073741824;
    /*]]>*/
    // applying the effect for every form
    var forms = document.querySelectorAll('.dnd_box');
    Array.prototype.forEach.call(forms, function (form) {
        var input = form.querySelector('input[type="file"]'),
            label = form.querySelector('label'),
            cloneLabel = label.cloneNode(true),
            errorMsg1 = form.querySelector('.err1'),
            errorMsg2 = form.querySelector('.err2'),
            uploadButton = form.querySelector('.dnd_button'),
            restart = form.querySelectorAll('.dnd_restart'),
            droppedFiles = false,
            showFiles = function (files) {
                console.log("show");
                if (files.length !== 0) {
                    Array.prototype.forEach.call(files, function (file) {
                        if (file.size > maxUploadB) {
                            uploadButton.style.visibility = 'hidden';
                            form.classList.add('is-error');
                            errorMsg1.textContent = file.name;
                            errorMsg2.textContent = "File size must be less then " + formatBytes(maxUploadB) + ".";
                            return true;
                        }
                    });

                    if (!form.classList.contains('is-error')) {
                        uploadButton.style.visibility = 'visible';
                        label.textContent = files.length > 1
                            ? (input.getAttribute('data-multiple-caption') || '')
                                .replace('{count}', files.length)
                            : files[0].name;
                    }
                }
            };

        // letting the server side to know we are going to make an Ajax request
        var ajaxFlag = document.createElement('input');
        ajaxFlag.setAttribute('type', 'hidden');
        ajaxFlag.setAttribute('name', 'ajax');
        ajaxFlag.setAttribute('value', 1);
        form.appendChild(ajaxFlag);

        // automatically submit the form on file select
        input.addEventListener('change', function (e) {
            showFiles(e.target.files);
        });

        // drag&drop files
        form.classList.add('has-advanced-upload');

        ['drag', 'dragstart', 'dragend', 'dragover', 'dragenter', 'dragleave', 'drop'].forEach(function (event) {
            form.addEventListener(event, function (e) {
                // preventing the unwanted behaviours
                e.preventDefault();
                e.stopPropagation();
            });
        });
        ['dragover', 'dragenter'].forEach(function (event) {
            form.addEventListener(event, function () {
                form.classList.add('is-dragover');
            });
        });
        ['dragleave', 'dragend', 'drop'].forEach(function (event) {
            form.addEventListener(event, function () {
                form.classList.remove('is-dragover');
            });
        });

        form.addEventListener('drop', function (e) {
            droppedFiles = e.dataTransfer.files; // the files that were dropped
            showFiles(droppedFiles);
        });

        // if the form was submitted
        form.addEventListener('submit', function (e) {
            // preventing the duplicate submissions if the current one is in progress
            if (form.classList.contains('is-uploading')) return false;
            form.classList.add('is-uploading');
            form.classList.remove('is-error');
            e.preventDefault();

            // gathering the form data
            var ajaxData = new FormData(form);
            if (droppedFiles) {
                Array.prototype.forEach.call(droppedFiles, function (file) {
                    ajaxData.append(input.getAttribute('name'), file);
                });
            }

            // ajax request
            var ajax = new XMLHttpRequest();
            ajax.open(form.getAttribute('method'), form.getAttribute('action'), true);

            ajax.onload = function () {
                form.classList.remove('is-uploading');
                uploadButton.style.visibility = 'hidden';
                if (ajax.status >= 200 && ajax.status <= 400) {
                    var data = JSON.parse(ajax.responseText);
                    form.classList.add(data.error === true ? 'is-error' : 'is-success');
                    if (!data.success) {
                        errorMsg1.textContent = data.errorName;
                        errorMsg2.textContent = data.errorMsg;
                    }
                } else {
                    showFiles(droppedFiles);
                    alert('Server Error. Please, contact the support!');
                }
            };

            ajax.onerror = function () {
                form.classList.remove('is-uploading');
                uploadButton.style.visibility = 'hidden';
                alert('Error! Please, try again!');
            };

            ajax.send(ajaxData);
        });

        // restart the form if has a state of error/success
        Array.prototype.forEach.call(restart, function (entry) {
            entry.addEventListener('click', function (e) {
                if (e.target.files === undefined) {
                    uploadButton.style.visibility = 'hidden';
                }
                label.innerHTML = cloneLabel.innerHTML;
                e.preventDefault();
                form.classList.remove('is-error', 'is-success');
                // input.click();
            });
        });

        // Firefox focus bug fix for file input
        input.addEventListener('focus', function () {
            input.classList.add('has-focus');
        });
        input.addEventListener('blur', function () {
            input.classList.remove('has-focus');
        });
    });
}(document, window, 0));

function formatBytes(bytes, decimals) {
    if (bytes === 0) return '0 Bytes';
    var k = 1024,
        dm = decimals <= 0 ? 0 : decimals || 2,
        sizes = ['Bytes', 'KB', 'MB', 'GB', 'TB', 'PB', 'EB', 'ZB', 'YB'],
        i = Math.floor(Math.log(bytes) / Math.log(k));
    return parseFloat((bytes / Math.pow(k, i)).toFixed(dm)) + ' ' + sizes[i];
}