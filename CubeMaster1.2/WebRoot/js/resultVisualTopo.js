(function(){
    var visualCount = 0;  //记录可视化是否被拖入
    var dataCount = 0; //记录数据是否被拖入
    /*规则 数据 可视化 保存对象*/
    var selectedMsg = {
        result_id : '',
        original_id:'',
        relationship:[],
        visual_col : [],   //存visual id
        method_name : '',
        rule_id:''
    };
    $(document).ready(function(){
        /*储存连线关系*/
        var connectionArrys = [],
            updateConnections = function (conn, remove) {
                if(!remove) connectionArrys.push(conn);
                else{
                    var idx=-1;
                    for(var i = 0;i<connectionArrys.length;i++){
                        if(connectionArrys[i]==conn){
                            idx = i;
                            break;
                        }
                    }
                    if(idx!=-1) connectionArrys.splice(idx,1);
                }
                return;
            };

        $.ajax({
            type:'post',
            dataType:'json',
            url:'../servlet/VisualizationServlet',
        }).success(function(data){
                dataVisualDom(data);
            }
        ).then(function(data){
                /*处理数据异步加载drag和drop失效的问题*/
                Listener.dragListener();
                jsPlumbAction(data);
            }
        );
        function dataVisualDom(data){
            var resultArry = data.result, /*结果数据数组*/
                originalArry = data.dataset, /*原始数据数组*/
                visualArry = data.visual, /*可视化数据数组*/
                ruleArry = data.rule;/*规则数组*/
            console.log(originalArry[0]);

            var ruleWrap = $(".ruleWrap"),
                ruleSelect = $(".ruleSelect"),
                ruleSelectSingle = $(".ruleSelectSingle");
            var attrName = 0;
            /*规则dom插入*/
            for(var i = 0,len = ruleArry.length; i < len; i++){
                var ruleSelectSingleWrap='';
                ruleWrap.append('<div class="rule menuewindow" data-attr="'+attrName+'">'+'<span class="rule-text">'+ruleArry[i].rule_type+'</span></div>');
                /*插入二、三级参数*/
                for(var j = 0,length = ruleArry[i].rules.length;j < length;j++){
                    var lis='';
                    for(var k = 0,paramLen = ruleArry[i].rules[j].arguments.length;k < paramLen;k ++){
                        lis+='<li class="paramBox" data-argument-sequence='+ruleArry[i].rules[j].arguments[k].argument_sequence+' data-type='+ruleArry[i].rules[j].arguments[k].argument_type+'>'+
                            '<div class="typeContent"><p>'+ruleArry[i].rules[j].arguments[k].argument_type+'</p></div><div class="paramTitle"><p>'+ruleArry[i].rules[j].arguments[k].argument_description+'</p></div><div class="paramContent"></div></li>';
                    }
                    ruleSelectSingleWrap=$('<div class="ruleSelectSingleWrap" data-attr='+attrName+'><div class="ruleSelectSingle menuewindow"  data-rule-id='+ruleArry[i].rules[j].rule_id+' data-visual-id='+ruleArry[i].rules[j].visual_id+' data-method-name='+ruleArry[i].rules[j].method_name+'>'+
                        '<span class="ruleSelectSingleName">'+ruleArry[i].rules[j].rule_name+'</span><i class="iconfont closediv">&#xe600;</i><ul class="paramBoxList">'+lis+'</ul></div></div>');

                    ruleSelect.append(ruleSelectSingleWrap);
                };
                attrName++;

            }
            /*结果数据dom插入*/
            var $selectresult = $('.select-result');
            for(var i = 0,len = resultArry.length; i<len; i++){
                var dataid = resultArry[i].id;

                var listchild = '';
                for(var j = 0,length = resultArry[i].attributes.length; j<length; j ++){
                    var dataCildId = resultArry[i].attributes[j].attribute_id;
                    listchild +='<li class="listchild" data-id='+dataCildId+' data-type='+resultArry[i].attributes[j].attribute_type+' data-sequence='+resultArry[i].attributes[j].attribute_sequence+'>'+
                        '<div class="BubuleBox">'+
                        '<p>'+resultArry[i].attributes[j].attribute_name+'</p></div></li>'
                }
                $selectresult.append('<div class="forul"><div class="window menuewindow " data-col-id= '+dataid+' data-type="result"><div class="indow_top">'+
                    '<div class="contentp"><p>'+resultArry[i].name+'</p></div>'+
                    '<i class="iconfont closediv">&#xe600;</i></div><i class="iconfont iconsize">&#xe602;</i><ul class="windowlist">'+listchild+'</ul></div></div>');
            }
            /*原始数据dom插入*/
            var $selectoriginal = $('.select-original');
            for(var i = 0,len = originalArry.length; i<len; i++){
                var dataid = originalArry[i].id;
                var listchild = '';
                for(var j = 0,length = originalArry[i].attributes.length; j<length; j ++){
                    var dataCildId = originalArry[i].attributes[j].attribute_id;
                    listchild +='<li class="listchild" data-id='+dataCildId+' data-type='+originalArry[i].attributes[j].attribute_type+' data-sequence='+originalArry[i].attributes[j].attribute_sequence+'>'+
                        '<div class="BubuleBox">'+
                        '<p>'+originalArry[i].attributes[j].attribute_name+'</p></div></li>'
                }
                $selectoriginal.append('<div class="forul"><div class="window menuewindow " data-col-id= '+dataid+' data-type="original"><div class="indow_top">'+
                    '<div class="contentp"><p>'+originalArry[i].name+'</p></div>'+
                    '<i class="iconfont closediv">&#xe600;</i></div><i class="iconfont iconsize">&#xe602;</i><ul class="windowlist">'+listchild+'</ul></div></div>')
            }
            /*可视化dom插入*/
            var visualStr='';
            for(var i=0,len=visualArry.length;i<len;i++){
                visualStr+=visualDataTpl(visualArry[i],visualArry[i].visual_type);
            }
            $('.selecvisual').append(visualStr);
            function visualDataTpl(param,type){
                var option={
                    visualName:param.visual_type,
                };
                if(type=='表格'){
                    option.type='&#xe604;';
                }else if(type=='饼状图'){
                    option.type='&#xe606;';
                }else if(type=='柱状图'){
                    option.type='&#xe607;';
                }else if(type=='折线图'){
                    option.type='&#xe603;';
                }else if(type=='散点图'){
                    option.type='&#xe605;';
                }
                return tplEngine('<div class="visualWay">'+
                    '<div class="visualContent menuewindow remove" data-type={visualName}>'+
                    '<div class="visualtop">'+
                    '<div class="contentp">'+
                    '<p>{visualName}</p>'+
                    '</div>'+
                    '<i class="iconfont closediv">&#xe600;</i>'+
                    '</div>'+
                    '<i class="iconfont iconsize">{type}</i>'+
                    '</div>'+
                    '</div>',option);
            };



        };

        function jsPlumbAction(data){
            var resultArry = data.result, /*结果数据数组*/
                originalArry = data.dataset, /*原始数据数组*/
                visualArry = data.visual, /*可视化数据数组*/
                ruleArry = data.rule;/*规则数组*/

            jsPlumb.ready(function () {
                /*声明jsplumb实例*/
                var instance = jsPlumb.getInstance({
                    DragOptions: { cursor: 'pointer', zIndex: 2000 },
                    PaintStyle: { strokeStyle: '#666' },
                    EndpointHoverStyle: { fillStyle: "orange" },
                    HoverPaintStyle: { strokeStyle: "orange" },
                    EndpointStyle: { width: 20, height: 16, strokeStyle: '#666' },
                    Endpoint: "Rectangle",
                    Anchors: ["TopCenter", "TopCenter"],
                    Container: "content"
                });
                /*拖拽样式*/
                var exampleDropOptions = {
                    tolerance: "touch",
                    hoverClass: "dropHover",
                    activeClass: "dragActive"
                };
                /*连线样式*/
                var connectorPaintStyle = {
                    lineWidth: 2,
                    strokeStyle: "#000",
                    joinstyle:"round",
                    outlineColor: "#fff",
                    outlineWidth: 2
                };
                /*连线hover样式*/
                var connectorHoverStyle = {
                    lineWidth: 2,
                    strokeStyle: "red",
                    outlineWidth: 2,
                    outlineColor:"white"
                };
                /*点hover颜色*/
                var endpointHoverStyle = {
                    fillStyle:"#000"
                };
                /*点的样式和属性*/
                var exampleEndpoint = {
                    endpoint:"Dot",
                    paintStyle:{
                        strokeStyle:"#000",
                        fillStyle:"transparent",
                        radius: 3,
                        lineWidth:3
                    },
                    scope:"scopeexample",
                    isSource:true,
                    maxConnections:4,
                    isTarget: true,
                    connector:[ "Flowchart", { stub:[40, 60], gap:10, cornerRadius:5 } ],
                    connectorStyle: connectorPaintStyle,
                    hoverPaintStyle: endpointHoverStyle,
                    connectorHoverStyle: connectorHoverStyle,
                    dragOptions:{},
                    overlays:[
                        [ "Label", {
                            location:[0.5, 1.5],
                            label:"",
                            cssClass:"endpointSourceLabel"
                        } ]
                    ]
                };
                /*点坐标集，判断是否达到最大连线数*/
                var anchors = [
                        [1, 0.2, 1, 0],
                        [0.8, 1, 0, 1],
                        [0, 0.8, -1, 0],
                        [0.2, 0, 0, -1]
                    ],
                    maxConnectionsCallback = function (info) {
                        alert("Cannot drop connection " + info.connection.id + " : maxConnections has been reached on Endpoint " + info.endpoint.id);
                    };
                /*连线监听，储存和删除连线关系*/
                instance.bind("connection", function (info, originalEvent) {
                    updateConnections(info.connection);
                });
                instance.bind("connectionDetached", function (info, originalEvent) {
                    updateConnections(info.connection, true);
                });

                instance.bind("connectionMoved", function (info, originalEvent) {
                    updateConnections(info.connection, true);
                });




                var idnum = 1;
                var box = ['.window','.ruleSelectSingle','.visualContent'];
                var $box = $(box.toString());
                var count = 1;


                instance.addEndpoint($("#resultcontent"),{anchor:"LeftMiddle",isSource:false,paintStyle:{strokeStyle:"#000",fillStyle:"transparent",radius:4,lineWidth:4}},exampleEndpoint); //创建res点
                /*拖放区域接收拖放元素*/
                $("#content").droppable({
                    accept:$box,
                    drop: function(even,ui){
                        if(!exist('"'+"id"+idnum+'"')){
                            var id = "id" + idnum;
                            var left = parseInt(ui.offset.left);
                            var top = parseInt(ui.offset.top);
                            var name = ui.draggable[0].className;
                            var match = /\w+\b/gi.exec(name);
                            var result = match.toString();
                            var flag = false;
                            switch(result){
                                /*拖放的是rule元素*/
                                case "ruleSelectSingle":{
                                    $(this).append('<div class="ruleSelectSingle position remove" data-rule-id='+$(ui.helper).attr('data-rule-id')+' data-visual-id='+$(ui.helper).attr('data-visual-id')+' data-method-name='+$(ui.helper).attr('data-method-name')+' id="'+id+'">'+$(ui.helper).html()+'</div>');
                                    $("#"+id).css("left", left).css("top", top);
                                    instance.draggable(id);
                                    instance.addEndpoint($("#"+id), { anchor: "RightMiddle",isTarget:false}, exampleEndpoint);
                                    $('.paramBox').click(function(event){
                                        event.stopPropagation();
                                    });
                                    var $parameter = $('#content').find('.ruleSelectSingle');
                                    var $thisclose = $parameter.find('.closediv');
                                    $thisclose.css('display', 'block');
                                    var $child = $parameter.find('.paramBoxList');
                                    $child.css('display','block').addClass("listanimate");
                                    Listener.closeDivRotate();
                                    ruleDeal(ruleArry);
                                    visualFind($(ui.helper),visualArry);

                                    var cover = $('.ruleCover');
                                    cover.css('display','block');   //规则拖入，出现遮罩，不能继续拖入
                                    var visualCover = $('.visualCover');
                                    if(visualCount == 0){   //判断是否已拖入可视化方式
                                        visualCover.css('display','none');  //没有去掉遮罩
                                    }else{
                                        visualCover.css('display','block');  //有，遮罩继续存在
                                    }

                                }
                                    break;
                                /*拖放的是数据元素*/
                                case "window":{
                                    if($(ui.helper).attr('data-type')=="result"){
                                        $(this).append('<div class="window hoverclass position remove" data-col-id='+$(ui.helper).attr('data-col-id')+' data-type="result" id="' + id + '" >' + $(ui.helper).html() + '</div>');
                                    }else{
                                        $(this).append('<div class="window hoverclass position remove" data-col-id='+$(ui.helper).attr('data-col-id')+' data-type="original" id="' + id + '" >' + $(ui.helper).html() + '</div>');
                                    }

                                    $("#"+id).css("left", left).css("top", top);
                                    box.push("#"+id);
                                    instance.draggable(id);
                                    createEndpoint(id,instance,exampleEndpoint);
                                    Listener.textListener();
                                    Listener.closeDivRotate();
                                    Listener.openListListener();

                                    $("#content .window").find('.listchild').draggable({
                                        revert:'invalid'
                                    });
                                    var $parameter = $('#content').find('.window');
                                    var $thisclose = $parameter.find('.closediv');
                                    $thisclose.css('display', 'block');
                                    var $child = $parameter.find('.listchild');
                                    $child.css('display','block').addClass('listanimate');
                                    var cover = $('.dataCover');
                                    cover.css('display','block');  //数据拖入，出现遮罩，不能继续拖入
                                    dataCount++;
                                }

                                    break;
                                /*拖放的是可视化方式元素*/
                                case "visualContent":{
                                    $(this).append('<div class="visualContent position remove" id="'+id+'">'+$(ui.helper).html()+'</div>');
                                    $("#"+id).css("left", left).css("top", top);
                                    box.push("#"+id);
                                    createEndpoint(id,instance,exampleEndpoint);
                                    Listener.textListener();
                                    Listener.closeDivRotate();
                                    visualDeal(visualArry);
                                    Listener.visualListListener();

                                    var $parameter = $('#content').find('.visualContent');
                                    var $thisclose = $parameter.find('.closediv');
                                    $thisclose.css('display', 'block');
                                    var $child =$parameter.find('.visualchild');
                                    $child.css('display','block').addClass('listanimate');
                                    var cover = $('.visualCover');
                                    cover.css('display','block');  //可视化拖入，出现遮罩，不能继续拖入
                                    visualCount++;     // 表示可视化已拖入
                                }
                                    break;
                            };
                            idnum++;
                        }
                        /*设置拖动不能拖出父级元素*/
                        $("#"+id).draggable({
                            containment:"parent"
                        });
                    }
                });

                instance.bind("click", function (connection, originalEvent) {
                    instance.detach(connection);
                    console.log("Success!");
                });


                function exist(id){  //判断是否已存在id
                    var s = document.getElementById(id);
                    if(s){
                        return true;
                    }
                    else{
                        return false;
                    }
                };

                Listener.removeDiv(instance);
            });

            $('#run-btn').on('click',function(){  //运行按钮监听
                var checkPam = checkParams();
                var chekConn = chekConnection(connectionArrys);
                if(chekConn&&checkPam){
                    var length = selectedMsg.visual_col.length;
                    if(length > 0){
                        checkAndRun();
                    }else{
                        alert('请选择具体可视化方式');
                    }
                }else{
                    console.log("不能执行");
                    return;
                }
            })
        };
        Listener.textListener();
        Listener.ruleListener();

    });



    function ruleDeal(array){
        $('#content .ruleSelectSingle').on('click',function(){   //拖入的规则点击事件
            if($(this).find($(".paramBoxList")).hasClass("listanimate")){
                $(this).find($(".paramBoxList")).css("display","none").css("opacity",0).removeClass("listanimate");
            }
            else{
                $(this).find($(".paramBoxList")).css("display","block").addClass("listanimate");
            }


        });
        $('.paramBoxList').click(function(event){
            event.stopPropagation();  //阻止冒泡
        });
        $('#content').find('.paramBox').droppable({  //判断拖入参数是否匹配规则
            accept:function(dragObj){
                if(dragObj.attr('data-type')===$(this).attr('data-type')||$(this).attr('data-type')==='混合'){
                    return true;
                }else{
                    return false;
                }
            },
            drop:function(event,ui){
                if($(this).attr('data-type')===ui.helper.attr('data-type')||$(this).attr('data-type')==='混合'){
                    console.log('参数匹配');
                    $(this).find('.paramContent').append(ui.helper);
                    /*存入关系*/
                    var argumentSequence = $(this).attr('data-argument-sequence'),
                        attributeId=ui.helper.attr('data-id'),
                        attributeType=ui.helper.attr('data-type'),
                        attributeSequence=ui.helper.attr('data-sequence');
                    var attributeObj={
                        "attribute_id":attributeId,
                        "attribute_type":attributeType,
                        "attribute_sequence":attributeSequence,
                        "argument_sequence":argumentSequence
                    }
                    selectedMsg.relationship.push(attributeObj);
                    if(($(this).find('.paramContent').children()).length>0){
                        $(this).droppable({ disabled: true }); //拖入一个元素后设定不可再拖入
                    }
                }else{
                    alert('拖入的参数类型不匹配');
                }


            }
        });
    }
    function visualFind($this,visualArry){  //拖入规则匹配对应可视化方式
        var visualRelation={};    //储存可视化id对应具体大类
        for(var i = 0,len = visualArry.length ; i<len; i++){
            var visualtype=visualArry[i].visual_type;
            var arr=[];
            for(var j = 0,length = visualArry[i].visuals.length; j<length; j++){
                var id = visualArry[i].visuals[j].visual_id;
               arr.push(id);

            }
            var arrStr=arr.toString();
            visualRelation[arrStr]=visualtype;

        };
        /*根据拖拽的规则的visualId匹配相应的可视化Id*/
        var visualContents=$('.contentTop .selecvisual').find('.visualContent').parent();
        var visualId=$this.attr('data-visual-id').split(',');
        visualContents.hide();
        for(var ids in visualRelation){

            var arr=ids.split(',');
            for(var i= 0,len=visualId.length;i<len;i++){
                for(var j= 0,leng=arr.length;j<leng;j++){
                    if(visualId[i]==arr[j]){//如果匹配上，则显示对应可视化div
                        var type = visualRelation[ids];
                        $('.contentTop .selecvisual').find('.visualContent[data-type='+type+']').parent().show();
                    }
                }
            }

        }

    }
    function visualDeal(array){  //拖入可视化方式添加子类选项
        var $visualName=$('#content .visualContent:last').find('.contentp p').html();
        var visualArry=array;
        var itemWrap='';
        var item='';
        var $visualItem=$('#content .visualContent:last');
        for(var i = 0,len = visualArry.length;i < len;i ++){
            if(visualArry[i].visual_type==$visualName){
                for(var j = 0,itemLen = visualArry[i].visuals.length;j < itemLen;j ++){
                    item+=visuallistTpl(visualArry[i].visuals[j]);
                }
                $visualItem.append('<ul class="visuallist">'+item+'</ul>');
            };
        }


        function visuallistTpl(itemObj){
            var option={
                itemName:itemObj.visual_name,
                visual_id:itemObj.visual_id
            }
            return tplEngine('<li class="visualchild" data-visual-id="{visual_id}"><span>{itemName}</span></li>',option);
        }

    }

    /*创建div两边的点*/
    function createEndpoint(id,instance,exampleEndpoint){
        instance.addEndpoint(id, { anchor:"RightMiddle",isTarget:false}, exampleEndpoint);
        instance.addEndpoint(id, { anchor:"LeftMiddle",isSource:false}, exampleEndpoint);
        instance.draggable(id);

    }
    function checkAndRun(){   //封装数据
        var	selectedMsgToJson={
            "dataset":{
                "dataset_id":'',
                "flag":'',
                "attribute": ''
            },
            "rule":{},
            "visual":{
                "visual_id":selectedMsg.visual_col
            }
        };
        var ruleId=$('#content .ruleSelectSingle').attr('data-rule-id'),
            ruleName=$('#content .ruleSelectSingle').attr('data-method-name'),
            resultType=$('#content .window').attr('data-type');
        /*newId=selectedMsgToJson.dataset.dataset_id,
         newArray=selectedMsgToJson.visual.visual_id;*/
        selectedMsgToJson.rule.method_name=ruleName;
        selectedMsgToJson.rule.rule_id=ruleId;
        selectedMsgToJson.dataset.attribute=selectedMsg.relationship;
        if(resultType=='result'){
            selectedMsgToJson.dataset.dataset_id=parseInt($('#content .window').attr('data-col-id'));
            selectedMsgToJson.dataset.flag='result';
        }else{
            selectedMsgToJson.dataset.dataset_id=parseInt($('#content .window').attr('data-col-id'));
            selectedMsgToJson.dataset.flag='original';
        }
        console.log(selectedMsgToJson);
        selectedMsgToJson=JSON.stringify(selectedMsgToJson);
        console.log(selectedMsgToJson);
        $.ajax({
            type:'post',
            url:'../servlet/VisualResultDisplayServlet',
            dataType:'json',
            data:{"visual_details":selectedMsgToJson},
            success:function(res){
                var type = res.visuals[0].visual_type;
                var dataArry=res.data;                                              
                /*展示方式类型*/
                var graphType = {
                    // '表格':createTable,
                    '饼状图':createPie,
                    '柱状图':createLineBar,
                    '折线图':createLineBar,
                    '散点图':creatScatter
                }; 
                console.log(type);
                graphType[type](res.visuals,dataArry,type); 
            }
        });
        //selectedMsg.visual_col = [];  //运行成功后重置数组的值，以便下次提交运行
        //instance.remove($('#content .visualContent'));  //删除可视化方式，让用户重选

    }
    function chekConnection(connectionArrys){   //判断连线是否完成
        var len = connectionArrys.length;
        for(var i = 0; i<len; i++){
            if($(connectionArrys[i].target).attr("class").indexOf("window") >= 0 && $(connectionArrys[i].source).attr("class").indexOf("ruleSelectSingle") >= 0){
                console.log("规则数据连接成功");
                for(var j = 0; j<len; j++){
                    if($(connectionArrys[j].target).attr("class").indexOf("visualContent") >= 0 && $(connectionArrys[j].source).attr("class").indexOf("window") >= 0){
                        console.log("数据可视化连接成功");
                        for(var k = 0; k<len; k++){
                            if($(connectionArrys[k].target).attr("class").indexOf("resultContent") >= 0 && $(connectionArrys[k].source).attr("class").indexOf("visualContent") >= 0){
                                console.log("可视化和res连接成功");
                                console.log("可以向后台发送数据");
                                return true;
                            }
                        }
                        alert("请将可视化连到res点");
                        return false;
                    }
                }
                alert('请连接数据和可视化方式');
                return false;
            }
        }
        alert('请连接规则和数据');
        return false;
    }
    function checkParams(){   //判断参数是否拖入写完成
        var paramBoxs=$('#content .paramBox').length;
        var lis= $('#content .paramBox').find('li').length;
        console.log(paramBoxs);
        console.log(lis);
        if(paramBoxs===lis){
            return true;
        }else{
            alert('请填完所有参数');
            return false;
        }
    }

    var Listener = {
        /*设置规则、数据、可视化方式可拖拽*/
        dragListener:function(){
            $('.selecvisual').find('.visualContent').draggable({
                helper:"clone",
                revert:'invalid'
            });
            $('.ruleSelect').find('.ruleSelectSingle').draggable({
                helper:"clone",
                revert:"invalid"
            });
            $('.selectdata').find('.window').draggable({
                helper:"clone",
                revert:"invalid"
            });
        },
        /*文字过长显示动画*/
        textListener:function(){
            $('#content').on("mouseover",'.contentp',function(){
                var now = this;
                var oP = $(this).find('p');
                var parent = now.parentNode;
                var pWidth = parent.offsetWidth;
                var width = oP.width();
                if(width > pWidth) {
                    var movewidth = -(width - pWidth) + "px";
                    $(this).find("p").animate({left: movewidth}, 3000);
                    console.log('success!');
                };
            });
            $('#content').on("mouseout",'.contentp',function(){
                var now = this;
                var oP =$(this).find("p");
                oP.stop();
                oP.css({"left":"0"});
            });
        },
        /*关闭按钮添加旋转动画*/
        closeDivRotate:function(){
            $(".closediv").on("mouseover",function(){
                $(this).removeClass("animate2");
                $(this).addClass("animate1");
            });
            $(".closediv").on("mouseout",function(){
                $(this).removeClass("animate1");
                $(this).addClass("animate2");
            });
        },
        /*子数据添加显示动画*/
        openListListener:function(){
            $('.windowlist').click(function(event){
                event.stopPropagation();  //阻止冒泡
            });
            $('#content .window').on('click',function(){
                if($(this).find($(".listchild")).hasClass("listanimate")){
                    $(this).find($(".listchild")).css("display","none").css("opacity",0).removeClass("listanimate");
                }
                else{
                    $(this).find($(".listchild")).css("display","block").addClass("listanimate");
                }
            })
        },

        visualListListener:function(){
            /*可视化方式显示动画*/
            $(".visualContent").click(function(){

                if($(this).find($(".visualchild")).hasClass("listanimate")){
                    $(this).find($(".visualchild")).css("display","none").css("opacity",0).removeClass("listanimate");
                    $(this).bind('webkitAnimationEnd',function(){
                        $(this).find($('.visualchild').css("display","none"));
                    });
                }
                else{
                    $(this).find($(".visualchild")).css("display","block").addClass("listanimate");
                    $(this).unbind('webkitAnimationEnd');
                }

            });
            /*可视化方式选择监听事件*/
            var visualchild = document.getElementsByClassName('visualchild');
            var len = visualchild.length;
            for(var i = 0; i < len; i++){
                visualchild[i].flag =false;
            }
            $('.visualchild').click(function(event){
                var visualId = Number($(this).attr('data-visual-id'));
                if(this.flag){
                    this.flag = false;
                    this.style.border = 'solid 1px #000';
                    console.log('未选择');
                    selectedMsg.visual_col.splice($.inArray(visualId,selectedMsg.visual_col),1);  //再次点击删除id
                }else{
                    this.flag = true;
                    this.style.border = 'solid 1px red';
                    console.log('已选择');
                    selectedMsg.visual_col.push(visualId);   //选择后储存id
                }

                Listener.textListener();
                event.stopPropagation();//阻止冒泡
            });
        },
        /*事件委托,删除相应div事件*/
        removeDiv:function(instance){
            $("#content").on("click",'.closediv',function(){

                var oParent = this;
                while(oParent.className.indexOf('remove') < 0){
                    oParent = oParent.parentNode;
                }
                instance.remove(oParent.id);
                if($(oParent).hasClass("ruleSelectSingle")){
                    var cover = $('.ruleCover');
                    var visualCover = $('.visualCover');
                    var dataCover = $('.dataCover');
                    /*清数据*/
                    selectedMsg.relationship=[];

                    if(dataCount != 0){
                        instance.remove($('#content .window'));
                        dataCount = 0;
                        dataCover.css('display','none');
                    }

                    cover.css('display','none');   //删除规则，去掉遮罩，可继续拖入
                    if(visualCount == 0){   //判断可视化是否已拖入
                        visualCover.css('display','block');  //没拖入则出现遮罩
                    }else{
                        instance.remove($('#content .visualContent')); //已拖入则同时删除拖入的可视化
                        visualCount = 0;  //重置计数器表示现在未拖入可视化
                        selectedMsg.visual_col=[];
                    }
                }else if($(oParent).hasClass("window")){
                    var dataCover = $('.dataCover');
                    var ruleCover = $('.ruleCover');
                    var visualCover = $('.visualCover');
                    /*清数据*/
                    selectedMsg.relationship=[];
                    ruleCover.css('display','none');
                    visualCover.css('display','block');
                    dataCover.css('display','none');    //删除数据，去掉遮罩，可继续拖入
                    dataCount = 0;
                    visualCount = 0;
                    instance.remove($('#content .ruleSelectSingle'));
                    instance.remove($('#content .visualContent'));
                    selectedMsg.visual_col=[];
                }else if($(oParent).hasClass("visualContent")){
                    /*清数据*/
                    selectedMsg.visual_col=[];
                    var cover = $('.visualCover');
                    cover.css('display','none');  //删除可视化，去掉遮罩，可继续拖入
                    visualCount = 0;  //重置计数器表示现在未拖入可视化
                }
            });
        },
        /*规则大类点击监听事件*/
        ruleListener:function(){
            $('.selectrule').on('click','.rule',function(){
                $('.ruleWrap').hide();
                $('.ruleSelect').fadeIn();
                $('.ruleSelect').find('.ruleSelectSingleWrap').css('display','none');
                var select = $(this).attr('data-attr');
                console.log(select);
                $('.ruleSelect').find('.ruleSelectSingleWrap[data-attr ='+ select+']').fadeIn();
            });
            $('.selectrule').on('click','.backToRule',function(){
                //$('.ruleSelect').fadeOut();
                $('.ruleSelect').hide();

                $('.ruleWrap').show();
            });

            $('.selectdata').on('click','.data-result',function(){
                $('.select-original').css('display','none');
                $('.select-result').fadeIn();
            });
            $('.selectdata').on('click','.data-original',function(){
                $('.select-result').css('display','none');
                $('.select-original').fadeIn();

            });

        }


    };

    /*echarts图形*/
    /*创建条形图*/
    function createLineBar(dataset,dataArry,type){
        var len = dataset.length;
        /*当前只有一列数据*/
        dataset = dataset[0];
        domInstall(len,'条形图');
        var option = {
            backgroundColor:'rgb(245,245,245)',
            tooltip : {
                trigger: 'axis',
                formatter:'数据{b}：{c}个'
            },
            legend:{
                x:'left',
                orient:'horizontal',
                data:[]
            },
            toolbox: {
                show : true,
                feature : {
                    mark : {show: true},
                    dataZoom : {show: true},
                    dataView : {show: true},
                    magicType : {show: true, type: ['line', 'bar']},
                    restore : {show: true},
                    saveAsImage : {show: true}
                }
            },
            dataZoom : {
                show : true,
                realtime : true,
                height: 20
            },
            xAxis : [
                {
                    type : 'category',
                    boundaryGap : false,
                    data : function (){
                        var list = [];
                        for(var i = 0;i < dataArry.length;i ++){
                            list.push(dataArry[i].label);
                        }
                        return list;
                    }()
                }
            ],
            yAxis : [
                {   
                    type : 'value'
                }
            ],
            series : [
                {
                    name:'',
                    type:'line',
                    data:function (){
                        var list = [];
                        for(var i = 0,len = dataArry.length;i < len;i ++){
                            list.push(dataArry[i].value[0]);
                        }
                        return list;
                    }()
                }
            ],
            calculable:false
        };

        var barType = {
            '折线图':{
                '标准折线图':function(){
                    return option;
                }
            },
            '柱状图':{
                '标准柱状图':function(){
                    var barOption = cloneObject(option);
                    barOption.series[0].type = 'bar';
                    barOption.xAxis[0].boundaryGap = 'true';
                    return barOption;
                }
            }
        };
            
            console.log(dataset.visual_name);
            console.log(barType[type][dataset.visual_name]());
        for(var i = 0;i < len;i ++){
            echarts.init($('.bar').get(i)).setOption(barType[type][dataset.visual_name]());
        }

    }

    /*创建pie*/
    function createPie(dataset,dataArry){
        var  len = dataset.length; /*pie数量*/
        domInstall(len,'饼图');

        /*基本option*/
        var option = {
                backgroundColor:'rgb(245,245,245)',
                title:{
                    text:'正确率分布图',
                    x:'center'
                },
                tooltip:{
                    trigger:'item',
                    formatter:'{a}： <br/> {b}：{c}（{d}%）'
                },
                legend:{
                    x:'left',
                    orient:'vertical',
                    data:function(){
                        var list = [];
                        for(var i = 0;i < dataArry.length;i ++){
                            list.push(dataArry[i].label);
                        }
                        return list;
                    }()
                },
                toolbox:{
                    show:true,
                    feature:{
                        dataView:{show: true, readOnly: false},
                        magicType:{
                            show:true,
                            type:['bar','funnel'],
                            funnel: {
                                x: '30%',
                                width: '40%',
                                funnelAlign: 'center'
                            }
                        },
                        restore:{show:true},
                        saveAsImage:{show:true}
                    }
                },
                calculable:true,
                series:[
                    {
                        name:dataset[0].dataset_name,
                        type:'pie',
                        data:function(){
                            var list = [];
                            for(var i = 0;i < dataArry.length;i ++){
                                list.push({value:dataArry[i].value[0],name:dataArry[i].label});
                            }
                            return list;
                        }()
                    }
                ]
            };


        /*饼图具体类型*/
        var itemType = {
            '标准饼图':function(){
                return option;
            },
            '环形标准图':function(){
                var ringOption = cloneObject(option);
                ringOption.series[0].radius = ['50%','70%'];
                return ringOption;
            },
            '南丁格尔玫瑰图':function(){
                var roseOption = cloneObject(option);
                roseOption.series[0].radius = '70%';
                roseOption.series[0].roseType = 'radius';
                return roseOption;
            }
        };

        for(var i = 0;i < len;i ++){
            echarts.init($('.pie').get(i)).setOption(itemType[dataset[i].visual_name]());
        }


    }

    /*创建散点图*/    
    function creatScatter(dataset,dataArry){
        var len = dataset.length;/*散点图个数*/
        domInstall(len,'散点图');/*dom组装*/
        /*组装series*/
        function seriesCreate(dataset,dataArry){
            var data = dataArry,
                seriesScatter = [];
            for(var i = 0,len = data.length;i < len;i ++){
                seriesScatter[i] = {};
                seriesScatter[i].name = data[i].label;
                seriesScatter[i].type = 'scatter';
                seriesScatter[i].data = [];
                /*构造series里data值*/
                for(var j = 0, coordinateLen = data[i].value.length;j < coordinateLen;j ++){            
                    
                    seriesScatter[i].data[j] = data[i].value[j];
                }
            }
            return seriesScatter;
            
        }

        function minMax(){
            var data = seriesCreate(dataset,dataArry),
                list = [];
            for(var i = 0,len = data.length;i < len;i ++){
                for(var j = 0,numLen = data[i].data.length;j < numLen;j ++){
                    list.push(data[i].data[j][0]);
                }
            }
            return {
                min:Math.floor(Math.min.apply(null,list)),
                max:Math.ceil(Math.max.apply(null,list))
            };
        }


        var option = {
            backgroundColor:'rgb(245,245,245)',
            tooltip:{
                trigger:'item',
                formatter:'{a}： <br/>{c}'
            },
            toolbox:{
                show:true,
                feature:{
                    dataView:{show: true, readOnly: false},
                    dataZoom:{show:true},
                    mark:{show:true},
                    magicType:{
                        show:true,
                        type:['stack'],
                        funnel: {
                            x: '30%',
                            width: '40%',
                            funnelAlign: 'center'
                        }
                    },
                    restore:{show:true},
                    saveAsImage:{show:true}
                }
            },
            calculable:true,
            dataZoom : {
                show : true,
                realtime : true,
                height: 20
            },
            xAxis : [
                {
                    type : 'value',
                    boundaryGap : false,
                    min:minMax().min,
                    max:minMax().max
                }
            ],
            yAxis : [
                {  
                    type : 'value'
                }
            ],
            series:seriesCreate(dataset,dataArry)
        };
        var itemType = {
                '标准散点图':function(){
                return option;
            }
           
        };
        for(var i = 0;i < len;i ++){
            echarts.init($('.bar').get(i)).setOption(itemType[dataset[i].visual_name]());
        }

    }



    /*非table类型的dom组装*/ 
    function domInstall(len,type){
        var wrap = document.getElementById('graph-wrap'),
            str = '';

        if(type === '饼图'){
            for(var i = 0;i < len - 1;i ++){
                str += '<div  class="col-md-6"><div class="graph pie"></div></div>';
            }
            if(len % 2){
                str += '<div  class="col-md-6 col-md-offset-3"><div class="graph pie"></div></div>';
            }else{
                str += '<div  class="col-md-6"><div class="graph pie"></div></div>';
            }
        }else/* if(type === '条形图')*/{
            for(var i = 0;i < len;i ++){
                str += '<div class="col-md-10 col-md-offset-1"><div class="graph bar"></div></div>';
            }
        }
        wrap.innerHTML = str;
    }
    topSlide();
    /*滑动至顶部*/
    function topSlide(){
        $('#top-control').on('click',function(){
            $('body').animate({scrollTop:'0'},'normal');
        });
        $(window).scroll(function(){
            if($('body').scrollTop() === 0){
                $('#top-control').hide();
            }else{
                $('#top-control').show();
            }
        });
    }

    /*克隆对象*/
    function cloneObject(src) {
        /*对非引用类型*/
        if (src == null || typeof src != 'object') {
            return src;
        }
        /*对于日期类型*/
        if (src instanceof Date) {
            var clone = new Date(src.getDate());
            return clone;
        }
        /*对于Array*/
        if (src instanceof Array) {
            var clone = [];
            for (var i = 0, len = src.length; i < len; i++) {
                clone[i] = src[i];
            }
            return clone;
        }
        /*对于Object  ！！！递归*/
        if (src instanceof Object) {
            var clone = {};
            for (var key in src) {
                    clone[key] = cloneObject(src[key]);
            }
            return clone;
        }
    }


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
        return string;
    }
})()