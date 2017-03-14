window.onload=function(){
    testSpecialLetter("inputName");
    testFile("file");
    var up=document.getElementById('upload');
	var $prog=$('.progress');
	$prog.hide();
	/*获取用户id*/
	document.getElementById('userId').value = localStorage.getItem('userId');
    up.onclick=testAll;
    up.onclick = function(){
        if(testAll() === undefined){     	
            /*构造json*/
            var content = document,
                jsonData = {},
                dataset = {},
                attribute = [],
                data = getElementsByClass(content.getElementById('single-data'),'input-test'),
                patameter = getElementsByClass(content.getElementById('total-patameter'),'row'),
                ispublic = content.getElementsByName('ispublic');
            for(var i = 0,len = data.length;i < len;i ++){
                dataset[data[i].name] = data[i].value;
            }

            /*是否公开*/
            for(var i = 0,len = ispublic.length;i < len;i ++){
                if(ispublic[i].checked){
                    dataset[ispublic[i].name] = ispublic[i].value;
                }
            }
            jsonData.dataset = JSON.stringify(dataset);
            /*数据字段*/
            for(var i = 0,len = patameter.length;i < len;i ++){
                var num = getElementsByClass(patameter[i],'input-test'),
                    obj = {};
                for(var j = 0,numLen = num.length;j < numLen;j ++){
                    console.log(num[j].value);
                    obj[num[j].name] = num[j].value;
                }
                attribute.push(obj);
            }

            jsonData.attribute = JSON.stringify(attribute);
            console.log(jsonData);
            document.getElementsByName('updataset')[0].value = JSON.stringify(jsonData);
            console.log(document.getElementsByName('updataset')[0].value);

            /*表单上传*/
            $('#fid').submit();
            alert('上传成功！')
            location.href = 'dataPresent.html';
           


            /*$prog.show();
			var $progress= $(".progress-bar");                   //找到进度条
			 var internalAction= setInterval(function(){
            	 $.ajax({
	            	type:"get",
	            	url:"../servlet/UploadProgressServlet",
	            	async:false,
	            	dataType:'json',
	            	success:function(data){
	            		var bytesRead=data.bytesRead;
	            		var contentLength=data.contentLength;
	            		var result=(contentLength/bytesRead)*100;
	            		var rate=data.rate;
	            		console.log(data);
	            		console.log(result);
	            		$progress.css("width",result+"%");
	            		$progress.html(result+"%");
	            		if(result===100){
	            			$progress.css("width","100%");
	            			$progress.html("100%");
	            			clearInterval(internalAction);
								if(alert('上传成功！') === undefined){
									location.href = 'dataPresent.html';
								}
							
	            		}
	            		
	            	}
	            });
	            },100);*/
        }
    }
    var strAll=true;
    var strFile=true;
    var n=0;
    var name;
    var str = false;
    /*添加按钮*/
    var patameterAppend=document.getElementById('append');
    patameterAppend.onclick=function(){
        var parent=document.getElementById('total-patameter'),
            child=document.getElementById('patameter-main'),
            patameterAdd=child.cloneNode(true),
            remove=document.createElement('button');
        remove.className='btn btn-default remove';
        remove.style.type="button";
        remove.innerHTML="删除";
        patameterAdd.appendChild(remove);
        remove.onclick=function(){
            parent.removeChild(this.parentNode);
        }
        var cloneText=patameterAdd.getElementsByTagName('input');
        for(var i=0;i<cloneText.length;i++){
            cloneText[i].value='';
        }
        parent.appendChild(patameterAdd);
    }

    /*根据数据类型变换字段序列*/
    typeChange();
    function typeChange(){
        var type = document.getElementById('data-type'),
            attributeList = document.getElementById('total-patameter'),
            numberStr = getElementsByClass(attributeList,'data-sequence')[0].innerHTML,
            labelStr = '<select class="form-control input-test" name="attribute_sequence"> ' +
                                    '<option value="1">第一列</option>' + 
                                    '<option value="-1">最后一列</option></select>';
        type.onchange = function(){
            var sequenceList = getElementsByClass(attributeList,'data-sequence'),
                len = sequenceList.length;
            if( type.value === '1'){
                for(var i = 0;i < len;i ++){
                    sequenceList[i].innerHTML = labelStr;
                }
            }else{
                for(var i = 0;i < len;i ++){
                    sequenceList[i].innerHTML = numberStr;
                }
            }
        }
    }
    /*包名检测*/
   /* testPakgeName('pakge-name');
    function testPakgeName(id){
        var ele=document.getElementById(id);
        var a;
        ele.onblur=function(){
            var data=ele.value;
            var dataArry=data.split('.');
            n++;
            var re=/^\d*[A-z]+[\w]*(\.\d*[A-z]+[\w]*)*$/;
            if((!re.test(data)||!(isNaN(dataArry[0])))&&data!==null){
                var odiv=document.createElement('div');
                odiv.setAttribute("style","position: absolute;top:8px;left:450px;color:red");
                name=n+"q";
                a=name;
                odiv.setAttribute("id",name);
                odiv.innerHTML="<span>X 璇锋寜鐓ф牸寮忎功鍐欏寘鍚嶏紒</span>";
                ele.parentNode.parentNode.appendChild(odiv);
                strAll=false;
                str=true;
            }
        }
        ele.onfocus=function(){
            if(str){
                this.parentNode.parentNode.removeChild(document.getElementById(a));
                strAll=true;
            }
        }
    }*/
    function testSpecialLetter(id){
        var ele=document.getElementById(id);
        var a;
        ele.onblur=function(){
            n++;
            var re=/^[^\w-_\.\u4e00-\u9fa5]+$/;
            if(re.test(this.value)){
                var odiv=document.createElement('div');
                odiv.setAttribute("style","position: absolute;top:8px;left:450px;color:red");
                name=n+"q";
                a=name;
                odiv.setAttribute("id",name);
                odiv.innerHTML="X 请不要使用特殊字符!";
                ele.parentNode.parentNode.appendChild(odiv);
                strAll=false;
                str=true;
            }
        }
        ele.onfocus=function(){
            if(str){
                this.parentNode.parentNode.removeChild(document.getElementById(a));
                strAll=true;
            }
        }
    }
    /*文件格式验证*/
     function testFile(id){
        var fe=document.getElementById(id);
        var te=document.getElementById("text");
        var sum=0;
        fe.onchange=function(){
            n++;
            te.value=this.value.substring(this.value.lastIndexOf('\\') + 1);
            /*var data=this.value;
            var s=data.lastIndexOf('.');
            var type=data.substring(s+1);
            if(type!="txt"){
                name=n+"k";
                var odiv=document.createElement('div');
                odiv.setAttribute("style","position: absolute;top:8px;left:450px;color:red");
                odiv.setAttribute("id",name);
                odiv.innerHTML="<span>X 请上传txt文件</span>";
                fe.parentNode.parentNode.appendChild(odiv);
                strFile=false;
                sum++;
            }
            if(sum&&type=="txt"){
                this.parentNode.parentNode.removeChild(document.getElementById(name));
                strFile=true;
            }*/
        }
    }
    function testAll(){
        var barr=[],
            carr=[];
        var inputList=getElementsByClass(document,'input-test');
        for(var i = 0;i<inputList.length;i++){
            if(inputList[i].value==''){
                alert('请准确填完所有信息!');
                return false;
            }
        }
        var radioA=document.getElementsByName('ispublic');
        for(var i = 0;i<radioA.length;i++){
            if(!radioA[i].checked){
                barr.push(radioA[i]);
            }
        }
        if((barr.length===2)||(!strAll)/*||(!strFile)*/){
            alert("请准确填完所有信息！");
            return false;
        }

    }
    function getElementsByClass(parent,className){
        var oele=parent.getElementsByTagName('*');
        var re=new RegExp('\\b'+className+'\\b', 'i');
        var arr=[];
        for(var i=0;i<oele.length;i++){
            if(re.test(oele[i].className)){
                arr.push(oele[i]);
            }
        }
        return arr;
    }

}