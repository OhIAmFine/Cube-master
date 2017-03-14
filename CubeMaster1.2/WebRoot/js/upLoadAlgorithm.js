window.onload=function(){
	/*算法名字*/
	testSpecialLetter("inputName");
	/*关键字*/
	testSpecialLetter("inputKey");
	/*测试数据集*/
	testSpecialLetter("inputTest");
	/*包名检测*/
	testPakgeName('pakge-name');
	testFile("file");
	var up=document.getElementById('upload');
	var $prog=$('.progress');
	$prog.hide();
	/*获取用户id*/
	document.getElementById('userId').value = localStorage.getItem('userId');
	up.onclick = function(){
		if(testAll() === undefined){

			/*构造json*/
			var content = document,
				jsonData = {},
				algorithm = {},
				keyword = [],
				parameters = [],
				data = getElementsByClass(content.getElementById('single-data'),'input-test'),
				patameter = getElementsByClass(content.getElementById('total-patameter'),'row');

			for(var i = 0,len = data.length;i < len;i ++){
				algorithm[data[i].name] = data[i].value;
			}

			delete algorithm.key_word;

			/*是否公开*/
			var isPublic = content.getElementsByName('ispublic');

			for(var i = 0,len = isPublic.length;i < len;i ++){
                if(isPublic[i].checked){
                    algorithm[isPublic[i].name] = isPublic[i].value;
                }
            }

			jsonData.algorithm = JSON.stringify(algorithm);
			/*参数*/
			if(strAdd){
				// parameters = [];
				for(var i = 0,len = patameter.length;i < len;i ++){
					var obj = {},
						num = getElementsByClass(patameter[i],'patameter-value');
					for(var j = 0,numLen = num.length;j < numLen;j ++){
						obj[num[j].name] = num[j].value;
					}
					parameters.push(obj);
				}
			}

			jsonData.parameter = JSON.stringify(parameters);

			// jsonData.parameter = JSON.stringify(parameters);
			/*关键字*/
			var key = content.getElementById('inputKey'),
				pre = /\s+|,+|，+|\t+/,
				keyWords = key.value.replace(/^\s+|^\t+|\s+$|\t+$/,'').split(pre);

			for(var i = 0,len = keyWords.length;i < len;i ++){
				keyword[i] = {},
				keyword[i].keyword = keyWords[i];
			}

			jsonData.keyword = JSON.stringify(keyword);


            content.getElementsByName('upalgo')[0].value = JSON.stringify(jsonData);
            console.log( content.getElementsByName('upalgo')[0].value);
            console.log(jsonData);

			/*文件表单上传*/
            $('#fid').submit();
			alert('上传成功！');
			
			location.href = 'algorithmList.html';


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
									location.href = 'algorithmList.html';
								}
							
	            		}
	            		
	            	}
	            });
	            },100);*/
		}
		
//		return false;

	}
	var strAll=true;
	var strFile=true;
	var n=0;
	var name;
	var str = false;
	var strAdd;
	/*点击是  显示参数*/
	var Patameter=document.getElementsByName('has_parameter')[0];
	Patameter.onclick=function(){
		strAdd=true;
		var patameterList=document.getElementById('total-patameter');
		patameterList.style.display='block';
	}
	/*点击否 删除参数*/
	var noPatameter=document.getElementsByName('has_parameter')[1];
	noPatameter.onclick=function(){
		strAdd=false;
		var patameterList=document.getElementById('total-patameter');
		patameterList.style.display='none';
	}
	/*添加参数*/
	var patameterAppend=document.getElementById('append');
	patameterAppend.onclick=function(){
		var parent=document.getElementById('total-patameter');
		var child=document.getElementById('patameter-main');
		var patameterAdd=child.cloneNode(true);
		var remove = patameterAdd.getElementsByTagName('button')[0];
		remove.className='btn btn-default remove';
		remove.style.type="button";
		remove.innerHTML="删除";
		/*点击删除  删除当前*/
		remove.onclick=function(){
			parent.removeChild(patameterAdd);
		}
		var cloneText=patameterAdd.getElementsByTagName('input')
		for(var i=0;i<cloneText.length;i++){
			cloneText[i].value='';
		}
		parent.appendChild(patameterAdd);
	}
	/*包名.类名格式、字符检测*/
	function testPakgeName(id){
		var ele=document.getElementById(id);
		var a;
		ele.onblur=function(){
			var data=ele.value;
			var dataArry=data.split('.');
			n++;
			var re=/^\d*[A-z]+[\w]*(\.\d*[A-z]+[\w]*)+$/;
			if((!re.test(data)||!(isNaN(dataArry[0])))&&data!==''){
				var odiv=document.createElement('div');
				odiv.setAttribute("style","position: absolute;top:8px;left:450px;color:red");
				name=n+"q";
				a=name;
				odiv.setAttribute("id",name);
				odiv.innerHTML="<span>X 请按照格式书写包名！</span>";
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
				odiv.innerHTML="X 请不要使用特殊字符";
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
	/*判断文件*/
	function testFile(id){
		var fe=document.getElementById(id);
		var te=document.getElementById("text");
		var sum=0;
		fe.onchange=function(){
			n++;
			te.value=this.value.substring(this.value.lastIndexOf('\\') + 1);
			if(te.value){
				var data=this.value;
				var s=data.lastIndexOf('.');
				var type=data.substring(s+1);
				if(type!="jar"){
					name=n+"k";
					var odiv=document.createElement('div');
					odiv.setAttribute("style","position: absolute;top:8px;left:450px;color:red");
					odiv.setAttribute("id",name);
					odiv.innerHTML="X 请上传.jar文件";
					fe.parentNode.parentNode.appendChild(odiv);
					strFile=false;
					sum++;
				}
				if(sum &&type=="jar"){
					// this.parentNode.parentNode.removeChild(document.getElementById(name));
					strFile=true;
					/*for(var i = sum;i > 0; sum--){
						this.parentNode.parentNode.removeChild(document.getElementById(name));
					}*/
					while(sum){
						this.parentNode.parentNode.removeChild(document.getElementById(name));
						sum--;
					}
				}
			}else{
				te.value = "点击上传文件";
				while(sum) {
					this.parentNode.parentNode.removeChild(document.getElementById(name));
					sum --;
				}
			}
		}
	}
	function testAll(){
		var barr=[],
			carr = [];
		var inputList=getElementsByClass(document,'input-test');
		for(var i=0;i<inputList.length;i++){
			if(inputList[i].value==''){
				alert('请准确填完所有空格');
				return false;
			}
		}
		if(strAdd){
			var patameterAll=document.getElementById('patameter-main');
			var addList=patameterAll.getElementsByTagName('input');
			for(var i=0;i<addList.length;i++){
				if(addList[i].value===''){
					alert("请准确填完所有空格");
					return false;
				}
			}
		}

		var radioA=document.getElementsByName('ispublic');
		for(var i=0;i<radioA.length;i++){
			if(!radioA[i].checked){
				barr.push(radioA[i]);
			}
		}
		var radioB=document.getElementsByName('has_parameter');
		for(var i=0;i<radioB.length;i++){
			if(!radioB[i].checked){
				carr.push(radioB[i]);
			}
		}


		if((barr.length===2)||(carr.length===2)||(!strAll)||(!strFile)){
			alert("请准确填完所有空格"); 
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