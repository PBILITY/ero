<%@ page language="java"  contentType="text/html; charset=UTF-8" %>

<html>
<link href="http://libs.baidu.com/bootstrap/3.0.3/css/bootstrap.min.css" rel="stylesheet" />

<body>
<%--tomcat1
springmvc上传文件
<form name="form1" action="/mmall/manage/product/upload.do" method="post" enctype="multipart/form-data">
    <input type="file" name="upload_file" />
    <input type="submit" value="springmvc上传文件" />
</form>


富文本图片上传文件
<form name="form2" action="/mmall/manage/product/richtext_img_upload.do" method="post" enctype="multipart/form-data">
    <input type="file" name="upload_file" />
    <input type="submit" value="富文本图片上传文件" />
</form>
<br>
<a href="../page/demo.html">socket</a>
<button id="batchImportBtn" class="button">Progress</button>
<!-- Modal -->
<div id="batchImportModal" class="modal fade" role="dialog" aria-labelledby="gridSystemModalLabel">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h4 class="modal-title" id="myModalLabel">批量导入用户</h4>
            </div>
            <div class="modal-body">
                <div class="form-group" id="passwordDiv">
                    <label>选择用户数据文件</label>
                    <input class="form-control" type="file" id="batchFile">
                </div>
                <div class="progress progress-striped active" style="display: none">
                    <div id="progressBar" class="progress-bar progress-bar-info"
                         role="progressbar" aria-valuemin="0" aria-valuenow="0"
                         aria-valuemax="100" style="width: 0%">
                    </div>
                </div>
                <div class="form-group">
                    <input id="batchUploadBtn" type="submit" name="submit" class="btn btn-success" value="上传" />
                </div>
            </div>
        </div>
        <!-- /.modal-content -->
    </div>
    <!-- /.modal-dialog -->
</div>
<!-- /.modal -->--%>


<form method="post" action="/user/login.do">
    <input type="text" name="username"/>
    <input type="password" name="password"/>
    <input type="submit" value="login"/>
</form>
</body>
<script src="http://libs.baidu.com/jquery/2.0.0/jquery.min.js"></script>
<script src="http://libs.baidu.com/bootstrap/3.0.3/js/bootstrap.min.js"></script>

<script>
    $(function() {
        // $('#batchImportModal').modal('show');
        // 批量导入按钮
        $("#batchImportBtn").click(function(){
            $('#batchImportModal').modal('show');
        });
        // 上传按钮
        $("#batchUploadBtn").attr('disabled', true);
        // 上传文件按钮点击的时候
        $("#batchUploadBtn").click(function() {
            // 进度条归零
            $("#progressBar").width("0%");
            // 上传按钮禁用
            $(this).attr('disabled', true);
            // 进度条显示
            $("#progressBar").parent().show();
            $("#progressBar").parent().addClass("active");
            // 上传文件
            UpladFile();
        });

        // 文件修改时
        $("#batchFile").change(function() {
            $("#batchUploadBtn").val("上传");
            $("#progressBar").width("0%");
            var file = $(this).prop('files');
            if (file.length != 0) {
                $("#batchUploadBtn").attr('disabled', false);
            }

        });

        function UpladFile() {
            var fileObj = $("#batchFile").get(0).files[0]; // js 获取文件对象
            console.info("上传的文件："+fileObj);
            var FileController = "/eormega/manage/product/uploadProgress.do"; // 接收上传文件的后台地址
            // FormData 对象
            var form = new FormData();
            // form.append("author", "hooyes"); // 可以增加表单数据
            form.append("upload_file", fileObj); // 文件对象
            // XMLHttpRequest 对象
            var xhr = new XMLHttpRequest();
            xhr.open("post", FileController, true);
            xhr.onload = function() {
                // ShowSuccess("上传完成");
                $("#batchUploadBtn").attr('disabled', false);
                $("#batchUploadBtn").val("上传");
                $("#progressBar").parent().removeClass("active");
                $("#progressBar").parent().hide();
                //$('#myModal').modal('hide');
            };
            xhr.upload.addEventListener("progress", progressFunction, false);
            xhr.send(form);
        }
        function progressFunction(evt) {
            var progressBar = $("#progressBar");
            if (evt.lengthComputable) {
                var completePercent = Math.round(evt.loaded / evt.total * 100)+ "%";
                progressBar.width(completePercent);
                $("#batchUploadBtn").val("正在上传 " + completePercent);
            }
        }
    });
</script>
</html>
