function uploadImage(obj) {
    console.log("obj=" + obj)
    var f = $(obj).val();
    console.log("f=" + f);

    if (f == null || f == undefined || f == "") {
        return false;
    }
    if (!/\.(?:png|jpg|bmp|gif|PNG|JPG|BMP|GIF)$/.test(f)) {
        alert("类型必须是图片（.png|jpg|bmp|gif|PNG|JPG|BMP|GIF）");
        $(obj).val("");
        return false;
    }

       // 单图上传

    var formData = new FormData();
    // debugger
    console.log($(obj)[0].files[0])
    formData.append("file", $("#file")[0].files[0]);


     /* //  批量上传

    var formData = new FormData();
    // debugger
    console.log($(obj)[0].files[0])
    var files = $("#file")[0].files;
    for (var i = 0; i < files.length; i++) {
        formData.append("file", $("#file")[0].files[i]);
    }*/

        $.ajax({
            type: "POST",
            url: apiPath + "/admin/upload/uploadimg",
            data: formData,
            cache: false,
            processData: false,    //JQuery不处理发送数据
            // contentType : 'multipart/form-data',//（如果这样，会导致contentType没有边界boundary，导致文件解析失败，后台报错Could not parse multipart servlet request;）
            contentType: false,
            success: function (result) {
                if (result.code == "200") {
                    console.log(result.data);
                    alert("图片上传成功")
                    window.location.reload()
                }
            }
        });
}