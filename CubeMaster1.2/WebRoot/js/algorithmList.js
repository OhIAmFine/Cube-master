$(function(){
    // var hash = location.hash.substring(1);
    $.ajax({
        type:'post',
        url: urlConfig.algorithmList.AlgorithmListServlet,
        // url: (hash === '') ? '../servlet/AlgorithmListServlet' : '../servlet/AlgorithmListServlet?operate=' + hash,
        dataType:'json',
        async:false,
        success:function(data){

            createPage(data);
        }
    });

    swicthAlgo();

    //给算法分类切换添加事件
    function swicthAlgo(){
        document.getElementById("algo-select").onchange=function(){
            var oSelect=document.getElementById("algo-select"),
            oOpotionList=oSelect.getElementsByTagName("option"),
            reqUrl="";
            for(var i=0;i<oOpotionList.length;i++){
                if(oOpotionList[i].selected&&oOpotionList[i].innerHTML=="我的算法"){
                    reqUrl=urlConfig.algorithmList.AlgorithmListServlet + '?operate=getCurrentUserAlgorithms';
                    // location.hash = '#getCurrentUserAlgorithms';
                }
                if(oOpotionList[i].selected&&oOpotionList[i].innerHTML=="所有算法"){
                    reqUrl= urlConfig.algorithmList.AlgorithmListServlet;
                }
            }
            // location.hash = reqUrl.indexOf('=') > -1 ? reqUrl.substring((reqUrl.indexOf('=')) + 1) : '';

            loadAlgo(reqUrl);


        };
    }

    /*如果算法已经选择了*/
    
    // historyAlgo();
    /*算法分类的历史管理*/
    function historyAlgo(){
        var url = '';
        window.onhashchange = function(){
            var hash = location.hash.substring(1);
            url =  (hash === '') ? urlConfig.algorithmList.AlgorithmListServlet : urlConfig.algorithmList.AlgorithmListServlet + '?operate=' + hash;
            if(hash === ''){
                document.getElementsByTagName("option")[1].selected = true;
            }else{
                 document.getElementsByTagName("option")[0].selected = true;
            }
            loadAlgo(url);
        }
    }

    // 加载算法
    function loadAlgo(reqUrl){
        var oContainer=document.getElementById("parent");
        $.ajax({
            type:'post',
            url:reqUrl,
            dataType:'json',
            success:function(data){

                var str="",
                singleClassAlgo=data;
                // for(var i=0;i<singleClassAlgo.length;i++){
                //     str+=algoTpl(singleClassAlgo[i]);
                // }
                /*if(reqUrl.indexOf('?') > -1){
                    for(var algoClassName in singleClassAlgo){
                    str+=algoTpl(singleClassAlgo[algoClassName],algoClassName);
                    }
                    oContainer.innerHTML=str;
                }else{ 
                    document.getElementById('parent').innerHTML = '';
                    createDom(data);
                }*/
                for(var algoClassName in singleClassAlgo){
                    str+=algoTpl(singleClassAlgo[algoClassName],algoClassName);
                }
                oContainer.innerHTML=str;



                pickSelect("pick"); 
                // pickGiveup("fail");
                // algoDataSelect(data);
                selectedAlgo();
            }
        });
    }

    /*已经选择的算法*/
    function selectedAlgo(){
        for(var i = 0,len = $('.algorithm button').length;i < len;i ++){
            for(var j = 0,selectedAlgoLen = $('.select-algorithm a').length;j < selectedAlgoLen;j ++){
                if($('.algorithm button').eq(i).data('id') === $('.select-algorithm a').eq(i).data('id')){
                    $('.algorithm button').eq(i).addClass('active');
                }
            }
        }
    }

    /*组装每一类算法填写的模板*/
    function algoTpl(singleAlgos,algoClassName){
        var option={},
            str='<div class = "col-md-12">'+
            '<div class = "page-header">'+
            '<a href = "xxx--more.html"class = "pull-right lead" > more</a>'+
            '<h2>'+
            '<a href="分类算法--more.html">'+algoClassName+'</a>'+
            '</h2>'+
            '</div>',
            strFragment='<div class = "row" id = "content" style = "background-color: rgb(249, 249, 249);">'+
            '<div class = "col-md-5" >'+
            '<h3><b>{algoName}</b></h3>'+
            '</div>'+
            '<div class = "col-md-3">'+
            '<span><b>作者</b>:{author}</span>'+
            '</div>'+
            '<div class = "col-md-4">'+
            '<span><b> 最近更新时间 </b>:{date}</span>'+
            '</div>'+
            '<div class = "col-md-12"><p><b>简介</b>:{description}</p></div>'+
            '<div class = "col-md-2 abox"><b>类型</b>:{type}</div>'+
            '<div class = "col-md-10 bbox"> 关键词：{keyword}</div>'+
            '<button class = "btn btn-default pull-right btn-primary pick" data-id = "{algorithm_id}" data-name = {algoName} style = "margin: 10px;" >选择</button>';

        for(var i=0;i<singleAlgos.length;i++){
            option = {
                algoName:singleAlgos[i]["algorithm"]["algorithm_name"],
                author:singleAlgos[i]["user_name"],
                date:singleAlgos[i]["submit_datetime"],
                description:singleAlgos[i]["algorithm"]["description"],
                type:singleAlgos[i]["algorithm"]["associated_tasks"],
                keyword:singleAlgos[i]["keywords"]["keyword"],
                algorithm_id:singleAlgos[i]["algorithm"]["algorithm_id"]
            };
            console.log(strFragment);
            str+=tplEngine(strFragment,option);
            str+='<a class = "btn btn-default pull-right btn-primary" role = "button" href = "algorithmDetails.html" style = "margin: 10px;">\
                  详情</a></div>';
        }
        str+='</div>';
        return str;
    }
    

    

   /*回调函数*/
   function createPage(data){
    createDom(data);
    pickSelect("pick"); 
    // pickGiveup("fail");
} 

/*创建dom节点*/
    function createDom(data){
        var Arry=data.algorithm,
        parentDiv=document.getElementById('parent');
        parentDiv.style.marginBottom='50px';
        for(var i=0;i<Arry.length;i++){
            var algorithmArry=Arry[i].algorithms,
            wrapParent=document.createElement('div'),
            len;
            wrapParent.className='col-md-12';
            parentDiv.appendChild(wrapParent);
            var header=document.createElement('div');
            header.className='page-header';
            if(Arry[i].algorithm_class.search('分类')){
                header.innerHTML = '<a href=\"xxx--more.html\" class=\"pull-right lead\">more...</a>'+
                '<h2><a href=\"分类算法--more.html\">'+Arry[i].algorithm_class+'</a></h2>';
            }else{
                header.innerHTML='<a href=\"聚类算法--more.html\" class=\"pull-right lead\">more...</a>'+
                '<h2><a href=\"聚类算法--more.html\">'+Arry[i].algorithm_class+'</a></h2>';
            }
            wrapParent.appendChild(header);
            for(var j=0,len=algorithmArry.length;j<len;j++){
                var wrap=document.createElement('div');
                wrap.className="row";
                wrap.style.backgroundColor='rgb(249,249,249)';
                wrap.setAttribute("id","content");
                var nameBox=document.createElement('div');
                nameBox.className="col-md-5";
                nameBox.innerHTML="<h3 ><b>"+algorithmArry[j].algorithm.algorithm_name+"</b></h3>";
                wrap.appendChild(nameBox);
                var authorDiv=document.createElement('div');
                authorDiv.setAttribute("class","col-md-3");
                authorDiv.innerHTML="<span><b>作者</b>："+algorithmArry[j].user_name+"</span>";
                wrap.appendChild(authorDiv);
                var timeDiv=document.createElement('div');
                timeDiv.setAttribute("class","col-md-4");
                timeDiv.innerHTML="<span><b>最近更新时间</b>："+algorithmArry[j].algorithm.submit_datetime+"</span>";
                wrap.appendChild(timeDiv);
                var textDiv=document.createElement('div');
                textDiv.setAttribute("class","col-md-12");
                textDiv.innerHTML="<p><b>简介</b>:"+algorithmArry[j].algorithm.description+"</p>";
                wrap.appendChild(textDiv);
                var typeDiv=document.createElement('div');
                typeDiv.setAttribute("class","col-md-2 abox");
                typeDiv.innerHTML="<b>类型</b>:"+Arry[i].algorithm_class;
                wrap.appendChild(typeDiv);
                var keyDiv=document.createElement('div'),
                keyWord=algorithmArry[j].key_word,
                keyArry=[];
                //console.log(keyWord[0]);
                for(var k=0;k<keyWord.length;k++){
                    keyArry.push(keyWord[k].keyword);
                }
                var keyStr=keyArry.toString();
                keyDiv.setAttribute("class","col-md-10 bbox");
                keyDiv.innerHTML="关键词："+keyStr;
                wrap.appendChild(keyDiv);
                /*var failPick=document.createElement('button');
                failPick.innerHTML="放弃";
                failPick.className="btn btn-default pull-right fail";
                failPick.style.visibility="hidden";
                wrap.appendChild(failPick);*/
                var pickbtn=document.createElement('button');
                pickbtn.innerHTML="选择";
                pickbtn.className="btn btn-default pull-right btn-primary pick";
                pickbtn.style.margin="10px";
                pickbtn.dataset.id=algorithmArry[j].algorithm.algorithm_id;
                pickbtn.dataset.name=algorithmArry[j].algorithm.algorithm_name;
                wrap.appendChild(pickbtn);
                /* var btnDiv=document.createElement('div');
                //btnDiv.innerHTML="<button type=\"submit\" class=\"btn btn-default pull-right  btn-success\" id=\"btn\">详情</button>";
                btnDiv.innerHTML="<a role=\"button\" class=\"btn btn-default pull-right btn-primary\" href=\"#\">详情</a>";
                btnDiv.style.margin="10px";*/
                var list=document.createElement('a');
                list.className="btn btn-default pull-right btn-primary";
                list.setAttribute('role','button');
                list.style.margin="10px";
                list.innerHTML="详情";
                list.href='algorithmDetails.html';
                list.onclick=function(){
                    var oPrent = this,
                    str = false;
                    while(oPrent.tagName.toUpperCase() !== 'BUTTON'){
                        oPrent = oPrent.previousSibling;
                    }

                    localStorage.setItem('algorithm_id',oPrent.dataset.id);
                    /*var obj = {
                        "protocol":"A-2-3-request",
                        "algorithm_id":oPrent.dataset.id
                    };
                    $.ajax({
                        type:'get',
                        data:obj,
                        dataType:'json',
                        url:'../servlet/AlgorithmDetailsServlet',
                        success:function(data){
                            str = true;
                        }
                    });
                    alert(1);
                    return false;*/
                }
                wrap.appendChild(list);
                var type=Arry[i].algorithm_class;
                wrapParent.appendChild(wrap);
            }
        }
        search();
    }    

function search() {
	var col_md_12 = document.getElementsByClassName('col-md-12');
	var col_md_12_len =  col_md_12.length;
	var text = document.getElementById('exampleInputName2');
	var search = document.getElementsByClassName('search')[0];
	
	text.onkeyup  = function (event) {
//		if ((event.keyCode == 13) || (event.which.keyCode == 13)) {
			var value = text.value,
			 	i,
			 	j,
			 	regex = new RegExp(value , 'i');
			for(j = 0 ;j<col_md_12_len ;j++) {
				var col_md_5 = col_md_12[j].getElementsByClassName('col-md-5');
				var row = col_md_12[j].getElementsByClassName('row');
				var col_md_5_len = col_md_5.length;
				
				for(i = 0 ;i < col_md_5_len ; i++){
					row[i].style.display = 'block';
					var gettest = col_md_5[i].getElementsByTagName('b')[0].innerHTML;
					if (!regex.test(gettest)) {
						row[i].style.display = 'none';
					}
				}
			
			}
//		}
	};
	search.onclick = function (event) {
		var value = text.value,
		 	i,
		 	j,
		 	regex = new RegExp(value , 'i');
		for(j = 0 ;j<col_md_12_len ;j++) {
			var col_md_5 = col_md_12[j].getElementsByClassName('col-md-5');
			var row = col_md_12[j].getElementsByClassName('row');
			var col_md_5_len = col_md_5.length;
			
			for(i = 0 ;i < col_md_5_len ; i++){
				row[i].style.display = 'block';
				var gettest = col_md_5[i].getElementsByTagName('b')[0].innerHTML;
				if (!regex.test(gettest)) {
					row[i].style.display = 'none';
				}
			}
		
		}

	};
}

/*判断选择的按钮*/
function pickSelect(className){
    var allElement=document.getElementsByClassName(className),
    len;
    for(var i=0,len=allElement.length;i<len;i++){
        allElement[i].onclick=function(){
            var dataId = {
                "algorithm_id":this.dataset.id
            };
            if(this.className.indexOf('active')!==-1) return;
            else{
                this.className+=" active";
                $.ajax({
                    type:'post',
                    url: urlConfig.algorithmList.StoreServlet,
                    dataType:'json',
                    data:dataId
                });
                animation(this,document.getElementById('pic').getElementsByTagName('span')[0],'algorithm');
                }
            }
        }
    }

    /*选择放弃按钮*/
    /*function pickGiveup(className){
        var allFail=document.getElementsByClassName('fail');
        for(var i=0;i<allFail.length;i++){
            allFail[i].onclick=function(){
                var algorithmsId=this.nextElementSibling.dataset.id;
                var storageA=localStorage.getItem('selectedAlgos');
                var storageArry=storageA.split(',');
                var index=storageArry.indexOf(algorithmsId);
                var storageNew=storageArry.splice(index,1);
                storageNew=storageArry.toString();
                localStorage.setItem('selectedAlgos',storageNew);
                this.style.visibility="hidden";
                this.nextElementSibling.innerHTML="选择";
                this.nextElementSibling.className=this.nextElementSibling.className.replace('active',"");
            }
        }
    }*/

    /**
     * 模板引擎
     * @param  {string} string 模板字符串
     * @param  {Object} option 参数对象
     * @return {string}        构造好的字符串
     */
     function tplEngine(string,option){
         for (var single in option){
             string = string.replace(new RegExp('{' + single + '}', 'g'), option[single]);
         }
       // console.log(string);
       return string;
   }

})