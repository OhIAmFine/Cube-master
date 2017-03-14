$(function(){
    $.ajax({
        type:'post',
        url:'../servlet/DataSetListServlet',
        dataType:'json',
        async:false,
        success:function(data){
            createPage(data);
        }
    });

    swicthData();

    //给算法分类切换添加事件
    function swicthData(){
        var frame = document.getElementById('frame');
        document.getElementById("data-select").onchange=function(){
            var oSelect=document.getElementById("data-select"),
            oOpotionList=oSelect.getElementsByTagName("option"),
            reqUrl="";
            for(var i=0;i<oOpotionList.length;i++){
                if(oOpotionList[i].selected&&oOpotionList[i].innerHTML=="我的数据"){
                    reqUrl='../servlet/DataSetListServlet?operate=getCurrentUserDataSet';
                }
                if(oOpotionList[i].selected&&oOpotionList[i].innerHTML=="所有数据"){
                    reqUrl='../servlet/DataSetListServlet';
                }
            }
            loadData(reqUrl);
        };
    }

    // 加载算法
    function loadData(reqUrl){
        var oContainer=document.getElementById("content");
        $.ajax({
            type:'post',
            url:reqUrl,
            dataType:'json',
            success:function(data){
                var str="",
                singleDatas=data;
                for(var i=0;i<singleDatas.length;i++){
                    // str+=dataTpl(singleClassData[DataClassName],DataClassName);
                    str+=dataTpl(singleDatas[i]);
                }
                oContainer.innerHTML=str;
                setDataId();
                pickSelect('test');

                selectedDataset();
            }
        });
    }

    /*已经选择的数据*/
    function selectedDataset(){
        for(var i = 0,len = $('.dataset button').length;i < len;i ++){
            for(var j = 0,selectedAlgoLen = $('.select-dataset a').length;j < selectedAlgoLen;j ++){
                if($('.dataset button').eq(i).data('id') === $('.select-dataset a').eq(i).data('id')){
                    $('.dataset button').eq(i).addClass('active');
                }
            }
        }
    }

    function setDataId(){
        var btnDetailed=$("#content a");
        for(var i=0;i<btnDetailed.length;i++){
            btnDetailed[i].onclick = function(){
                var oParent = this.parentNode;
                while(oParent.tagName.toUpperCase() !== 'BUTTON'){
                    oParent = oParent.nextSibling;
                }
                localStorage.setItem('dataset_id',oParent.dataset.id);
            }

        }
    }

    /*组装每一类算法填写的模板*/
    function dataTpl(singleData){

        var option={},
        str='';
        strFragment='<div class="container">'+
        '<h2>{dataName}</h2>'+
        '<div class="row">'+
        '<div class="col-md-6">{description}</div>'+
        '<div class="col-md-1">'+
        '<a class="btn btn-success btn-lg" href="dataDesc.html">详情</a>'+
        '</div>'+
        '<button class="btn btn-success btn-lg test" data-id="{dataId}"  data-name={dataName} style="margin-right: 10px;">选择</button>'+
        '</div>'+
        '</div>';

        option={
            dataName:singleData["dataSet"]["dataset_name"],
            author:singleData["user_name"],
            date:singleData["submit_datetime"],
            description:singleData["dataSet"]["description"],
            dataId:singleData["dataSet"]["dataset_id"]
        };
        str=tplEngine(strFragment,option);
        return str;
    }


    /*回调函数*/
    function createPage(data){
        createDom(data);
        pickSelect('test');
    
    }

    /*dom节点创建*/
    function createDom(data){
        var main=document.getElementById('content');
        var dataArry=data.dataset;
        for(var i=0;i<dataArry.length;i++){
            var wrap=document.createElement('div');
            wrap.className="container";
            main.appendChild(wrap);
            var title=document.createElement('h2');
            title.innerHTML=dataArry[i].dataset.dataset_name;
            wrap.appendChild(title);
            var dataWrap=document.createElement('div');
            dataWrap.className="row";
            wrap.appendChild(dataWrap);
            var dataDiv=document.createElement('div');
            dataDiv.className="col-md-6";
            dataDiv.innerHTML=dataArry[i].dataset.description;
            dataWrap.appendChild(dataDiv);
            var btnWrap=document.createElement('div');
            btnWrap.className="col-md-1"
            dataWrap.appendChild(btnWrap);
            var btnDetailed=document.createElement('a');
            btnDetailed.className="btn btn-success btn-lg";
            btnDetailed.innerHTML='详情';
            btnDetailed.href='dataDesc.html';
            btnDetailed.onclick = function(){
                var oParent = this.parentNode;
                while(oParent.tagName.toUpperCase() !== 'BUTTON'){
                    oParent = oParent.nextSibling;
                }
                localStorage.setItem('dataset_id',oParent.dataset.id);
            }
            btnWrap.appendChild(btnDetailed);
            var btnPick=document.createElement('button');
            btnPick.className="btn btn-success btn-lg test";
            btnPick.dataset.id=dataArry[i].dataset.dataset_id;
            btnPick.dataset.name=dataArry[i].dataset.dataset_name;
            btnPick.innerHTML='选择';
            btnPick.style.marginRight="10px";
            dataWrap.appendChild(btnPick);
        }
        search();
    }

    /*点击选择按钮*/
    function pickSelect(className){
        var allElement=document.getElementsByClassName(className),
        len;
        for(var i=0,len=allElement.length;i<len;i++){
            allElement[i].onclick=function(){
            	var dataId = {
                    "dataset_id":this.dataset.id
                };
                if(this.className.indexOf('active')!==-1) return;
                else{
                    this.className+=" active";
                    /*向后台发送数据id*/
                    $.ajax({
                        type:'post',
                        url:'../servlet/StoreServlet',
                        dataType:'json',
                        data:dataId
                    });
                }
                animation(this,document.getElementById('pic').getElementsByTagName('span')[0],'dataset');
                // $('.dataset ul').append(daTpl({
                //     dataset_name:$(this).data('name'),
                //     dataset_id:$(this).data('id')
                // },'dataset'));
            }
        }
    }
    //搜索功能正则匹配
    function search() {
    	var search = document.getElementsByClassName('search')[0];
    	var text = document.getElementById('exampleInputName2');
    	var container = document.getElementsByClassName('container');
    	var length = container.length;
    	text.onkeyup  = function (event) {
//    		if ((event.keyCode == 13) || (event.which.keyCode == 13)) {
    			var value = text.value,
    			 	i,
    			 	regex = new RegExp(value , 'i');
    			for(i = 1 ;i < length+1 ; i++){
    				container[i].style.display = 'block';
    				var gettest = container[i].getElementsByTagName('h2')[0].innerHTML;
    				if (!regex.test(gettest)) {
    					container[i].style.display = 'none';
    				}
//    			}
    		}
    	};
    	search.onclick = function (event) {
    			var value = text.value,
    			 	i,
    			 	regex = new RegExp(value , 'i');
    			for(i = 1 ;i < length+1 ; i++){
    				container[i].style.display = 'block';
    				var gettest = container[i].getElementsByTagName('h2')[0].innerHTML;
    				if (!regex.test(gettest)) {
    					container[i].style.display = 'none';
    				}
    			}
    	};
    	
    }


    /*点击放弃*/
    /*function pickGiveup(className){
        var allFail=document.getElementsByClassName('fail');
        for(var i=0;i<allFail.length;i++){
            allFail[i].onclick=function(){
                var DatarithmsId=this.previousElementSibling.dataset.id;
                var storageA=localStorage.getItem('selectedDatasets');
                var storageArry=storageA.split(',');
                var index=storageArry.indexOf(DatarithmsId);
                var storageNew=storageArry.splice(index,1);
                storageNew=storageArry.toString();
                localStorage.setItem('selectedDatasets',storageNew);
                this.style.visibility="hidden";
                this.previousElementSibling.innerHTML="放弃";
                this.previousElementSibling.className=this.previousElementSibling.className.replace(' active',"");
            }
        }
    } */

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