$(function(){
    /*保存连个选项的用户选择，保存项的id*/
    var selectedMsg = {
        result_id : '',
        result_col : [/*{
            attribute_id:'',
            attribute_sequence:'',
            attribute_type:'',
            argument_sequence:''
        }*/],
        original_id:'',
        original_col:[/*{
            ttribute_id:'',
            attribute_sequence:'',
            attribute_type:'',
            argument_sequence:''
        }*/],
        visual_col : [],
        rule : '',
        rule_id:''
    };

    
    $.ajax({
        type:'post',
        dataType:'json',
        url:'../servlet/VisualizationServlet',
        success:function(data){
            dataVisualDom(data);
            addDisabled();
        }
    });

    /*组装dom元素*/
    function dataVisualDom(data){

        var resultWrap = document.getElementById('result-data'),
            originalWrap = document.getElementById('original-data'),
            visualWrap = document.getElementById('visual-data'),
            ruleWrap = document.getElementById('rule-data'),

            resultArry = data.result, /*结果数据数组*/
            originalArry = data.dataset, /*原始数据数组*/
            visualArry = data.visual, /*可视化数据数组*/
            ruleArry = data.rule,/*规则数组*/

            strResult = '', /*结果*/
            strOriginal = '',/*原始*/
            strVisual  = '',/*可视化*/
            strRule = '';/*规则*/
        /*结果数据*/
        
        for(var i = 0,len = resultArry.length;i < len;i ++){
            var str = '';
            for(var j = 0,itemLen = resultArry[i].attributes.length;j < itemLen;j ++){
                str += dataItemTpl(resultArry[i].attributes[j],'attribute','result');
            }
            strResult += dataTpl(resultArry[i],'列名','result') + str + '</div></li>';
        }
        resultWrap.innerHTML = strResult;


        /*原始数据*/
        for(var i = 0,len = originalArry.length;i < len;i ++){
            var str = '';
            for(var j = 0,itemLen = originalArry[i].attributes.length;j < itemLen;j ++){
                str += dataItemTpl(originalArry[i].attributes[j],'attribute','original');
            }
            strOriginal += dataTpl(originalArry[i],'列名','original') + str + '</div></li>';
        }

        originalWrap.innerHTML = strOriginal;
        
        /*可视化*/
        for(var i = 0,len = visualArry.length;i < len;i ++){
            var str = '';
            for(var j = 0,itemLen = visualArry[i].visuals.length;j < itemLen;j ++){
                str += dataItemTpl(visualArry[i].visuals[j],'visual','visual');
            }

            strVisual += dataTpl(visualArry[i],'方式','visual') + str + '</div></li>';
        }

        visualWrap.innerHTML = strVisual;   

        /*规则*/
        for(var i = 0,len = ruleArry.length;i < len;i ++){
            var str = '<div class="rule-col-selection"><div class="rule-col-intro">请选择具体方式</div>',
                paramStr = '';
            for(var j = 0,itemLen = ruleArry[i].rules.length;j < itemLen;j ++){
                str += ruleItemTpl(ruleArry[i].rules[j]);
                var str2 = '<div class="rule-item-param" data-item-id=' + ruleArry[i].rules[j].rule_id + '><div class="rule-col-intro">参数</div>';
                for(var k = 0,paramLen = ruleArry[i].rules[j].arguments.length;k < paramLen;k ++){
                    str2 += ruleParamTpl(ruleArry[i].rules[j].arguments[k]);
                }
                str2 += '</div>';
                paramStr += str2;
            }
            strRule += ruleTpl(ruleArry[i]) + str + '</div>' + paramStr +'</li>';
        }

        ruleWrap.innerHTML = strRule;

    }

    /*组装规则*/
    function ruleTpl(param){
        var option = {
            name:param.rule_type
        };
        return tplEngine('<li><a href="javascript:">{name}</a>' + 
                '<button role="button" class="btn btn-primary">选择</button>',option);
    }

    /*组装规则具体方式*/
    function ruleItemTpl(param){
        var option = {
            ruleId:param.rule_id,
            name:param.rule_name,
            visualId:param.visual_id,
            methodName:param.method_name
        };
        return tplEngine('<div class="rule-col" data-col-id={ruleId} data-method-name={methodName} data-visual-id={visualId}>{name}</div>',option);
    }

    /*组装规则参数*/
    function ruleParamTpl(param){
        var option = {
            id:param.argument_id,
            type:param.argument_type,
            description:param.argument_description,
            sequence:param.argument_sequence
        };
        return tplEngine('{type}：<div class="rule-item" data-description={description} data-type={type} data-sequence={sequence}>{description}</div>',option);
    }


    function dataTpl(param,sub,type){
        var option = {
            dataName:param.name || param.visual_type,
            btnId:param.id || 10,
            type:type,
            subName:sub
        };
        
        return tplEngine('<li><a href="javascript:">{dataName}</a>' + 
            '<button role="button" class="btn btn-primary" data-id={btnId}>选择</button>' + 
            '<div class="{type}-col-selection">' + 
            '<div class="{type}-col-intro">请选择参与展示的{subName}</div>',option);
        
    }

    /*组装单个列*/
    function dataItemTpl(param,sub,type){
        var option = {
            name:param[sub + '_name'],
            id:param[sub + '_id'],
            type:type,
            atrType:param[sub + '_type'],
            sequence:param[sub + '_sequence']
        };
        return tplEngine('<div class="{type}-col" data-col-id={id} data-type={atrType} data-sequence={sequence}>{name}</div>',option);
    }
    


    //进入页面，为每一种可视化展示方式和数据添加class的值为disabled的属性
    function addDisabled(){
        $('.visual-col').addClass('disabled');
        $(".visual-col").addClass('btn');

        $('#original-data button').addClass('disabled');
        $('#result-data button').addClass('disabled');
    }


    /*事件委托，(原始数据/规则/可视化)所有选项里面每个li对应的btn*/
    $('.result-visual-selections').on('click','button',function(){
        /*如果是已经选中了的*/
        if( $(this).hasClass('active') ) return;

        var $this = $(this);
        /*拿到这个btn所属的选项(结果/可视化)*/
        var oParent = this;
        while( oParent.tagName.toUpperCase() !== 'DIV' ){
            oParent = oParent.parentNode;
        }
        /*选项对应的单词*/
        var optionName = oParent.id.substring(7);

        this.innerHTML = '已选择';

        /*数据可选了*/
        $('#original-data button').removeClass('disabled');
        $('#result-data button').removeClass('disabled');



        /*如果之前已经在这个选项选过一个了*/
        if( selectedMsg[optionName + '_id'] !== '' || optionName === 'rule'){
            /*清除选中效果*/
            var $oldSelect = $('#' + oParent.id + ' button.active');
            $oldSelect.removeClass('active');
            $oldSelect.html('选择');

            $oldSelect.siblings('.' + optionName + '-col-selection').slideUp('fast');

            $oldSelect.nextAll('.rule-item-param-active').slideUp('fast');
        }

        selectedMsg[optionName + '_col'] = [];

        if(optionName === 'rule'){
            /*可以选择的可视化id*/
            selectedMsg.visual_col = [];
            $('.visual-col-selection .visual-col').addClass('disabled');
            var visualSelect =  $(this).nextAll('.rule-col-selection').find('.rule-col-active');
            if(visualSelect.length !== 0){
                var visualSelectId = visualSelect.data('visualId').toString().split(','),
                    $visualAll = $('.visual-col-selection .visual-col');
                $visualAll.addClass('disabled');
                for(var i = 0,len = visualSelectId.length;i < len;i ++){
                    for(var j = 0,numLen = $visualAll.length;j < numLen;j ++){
                        if(visualSelectId[i] === $visualAll.eq(j).data('colId').toString()){
                            $visualAll.eq(j).removeClass('disabled');
                        }
                    }
                }
            }
            selectedMsg.rule = {};
                
        }

        if(oParent.id.indexOf('select') !== -1){
            $this.addClass('active');
            if(optionName !== 'visual'){
                delectData();
            }

           /* selectedMsg[optionName + '_col'] = [];*/

            /*如果之前已经点击了列名*/
            var selectedCols = $this.nextAll().find('.' + optionName + '-col-active').not('.disabled');
            selectedMsg[optionName + '_col'] = [];
            if(selectedCols.length){
                if(optionName === 'rule'){
                    $.each( selectedCols, function(key,value){
                        selectedMsg.rule = {
                            rule_id:value.dataset.colId,
                            method_name:value.dataset.methodName
                        };
                    })
                }else{
                    $.each( selectedCols, function(key,value){
                        selectedMsg[optionName + '_col'].push( value.dataset.colId );
                    })
                }
            }

            /*if(optionName !== 'visual'){
                delectData();
            }*/

            if(optionName !== 'visual' && optionName !== 'rule'){
                selectedMsg[optionName + '_id'] = this.dataset.id;
            }
            $this.siblings('.' + optionName + '-col-selection').slideDown('fast');
            if(selectedMsg.rule.rule_id){
                $(this).parent().find('.rule-item-param-active').slideDown('fast');
            }

        }

        console.log(selectedMsg);

        judSelectedAll();

    })

    /*清除匹配的数据*/
    function delectData(){
        /*规则方式的参数*/
        var $ruleParam =  $('#rule-data button.active').nextAll('.rule-item-param-active').find('.rule-item');
        $ruleParam.each(function(){
            $(this).removeClass('rule-item-active').removeClass('rule-item-danger').html($(this).data('description'));
        });

        /*删除选择的数据列*/
        $('.original-col-active').removeClass('original-col-active').removeData('sequence');
        $('.result-col-active').removeClass('result-col-active').removeData('sequence');
        selectedMsg.original_col = [];
        selectedMsg.result_col = [];
    }

    /*判断是否满足添加组合条件*/
    function judSelectedAll(){
        /*判断是否满足结果列的选择*/
        var $ruleParam =  $('#rule-data button.active').nextAll('.rule-item-param-active').find('.rule-item'),
            $ruleParamActive = $ruleParam.filter('.rule-item-active');

        if( (selectedMsg.result_col.length !== 0 ||selectedMsg.original_col.length !== 0) && $ruleParam.length === $ruleParamActive.length){
            $('.result-selected').css('visibility','visible');
        }else{
            $('.result-selected').css('visibility','hidden');
        }
        
        /*判断是否满足可视化列的选择 若是选择表格selectedMsg.visualCol为空*/
        if( selectedMsg.visual_col.length !== 0 || selectedMsg.visual_id === '0'){       
            $('.visual-selected').css('visibility','visible');
        }else{
            $('.visual-selected').css('visibility','hidden');
        }
 
        /*判断是否满足规则的选择*/
        if(selectedMsg.rule.rule_id){
            $('.rule-selected').css('visibility','visible');
        }else{
            $('.rule-selected').css('visibility','hidden');
        }

        /*判断是否选完了*/
        if($('.result-selected').css('visibility') === 'visible' && $('.rule-selected').css('visibility') === 'visible' && $('.visual-selected').css('visibility') === 'visible') {
            $('#add-group-btn').removeClass('disabled');
            return;
        }

        $('#add-group-btn').addClass('disabled');
    }


    /*添加组合按钮的事件*/
    $('#add-group-btn').on('click',function(){
        if( $(this).hasClass('disabled') ) return;
        var selectedMsgToJson=JSON.stringify(selectedMsg);


        var selectedMsgToJson = {
                    "dataset":{
                        "dataset_id":'',
                        "flag":'', 
                        "attribute": ''
                    },
                    "rule":selectedMsg.rule, 
                    "visual":{
                        "visual_id":selectedMsg.visual_col
                    }
            };

            if(selectedMsg.original_col.length !== 0){
                var selectData = selectedMsgToJson.dataset;
                selectData.flag = 'original';
                selectData.dataset_id = selectedMsg.original_id;
                selectData.attribute =  selectedMsg.original_col;
            }

            if(selectedMsg.result_col.length !== 0){
                var selectData = selectedMsgToJson.dataset;
                selectData.flag = 'result';
                selectData.dataset_id = selectedMsg.result_id;
                selectData.attribute = selectedMsg.result_col;
            }
            selectedMsgToJson = JSON.stringify(selectedMsgToJson);
            console.log(selectedMsgToJson);
        /*向后台发送数据*/
        $.ajax({
            type:'post',
            // url:'../servlet/StoreServlet',
            url:'../servlet/VisualResultDisplayServlet',
            dataType:'json',
            data:{"visual_details":selectedMsgToJson},
            success:function(res){
                var type = res.visuals[0].visual_type;
                var dataArry=res.data;                                              
                /*展示方式类型*/
                var graphType = {
                    '表格':createTable,
                    '饼状图':createPie,
                    '柱状图':createLineBar,
                    '折线图':createLineBar,
                    '散点图':creatScatter
                }; 
                console.log(type);
                graphType[type](res.visuals,dataArry,type); 
            }
        });

    })

    /*点击了运算结果的列名和具体图形*/
    $('.result-visual-selections').on('click','div',function(){
        var $this = $(this);

        /*确保拿到的是运算结果的列名和具体规则方式和具体图形*/
        if(this.dataset.colId){

            var oParent = this; 
            
            /*拿到这个div所属的选项(结果|可视化)*/
            while(oParent.id.indexOf('select') < 0){
                oParent = oParent.parentNode;
            }

            /*选项对应的单词*/
            var optionName = oParent.id.substring(7);

            /*选择了规则可以确定可视化具体方式状态*/
            if(optionName === 'rule'){

                /*可以选择的可视化id*/
                var visualSelect =  $(this).data('visualId').toString().split(','),
                    $visualAll = $('.visual-col-selection .visual-col');
                $visualAll.addClass('disabled');
                for(var i = 0,len = visualSelect.length;i < len;i ++){
                    for(var j = 0,numLen = $visualAll.length;j < numLen;j ++){
                        if(visualSelect[i] === $visualAll.eq(j).data('colId').toString()){
                            $visualAll.eq(j).removeClass('disabled');
                        }
                    }
                }

                delectData();

                /*如果之前已经点击了*/
                if($(this).hasClass('rule-col-active')) return ;
                $(this).parent().find('.rule-col-active').removeClass('rule-col-active');

                $(this).addClass('rule-col-active');
                selectedMsg.rule = {
                    rule_id:$(this).data('colId'),
                    method_name:$(this).data('methodName')
                };

                $(this).parent().nextAll('.rule-item-param-active').slideUp().removeClass('rule-item-param-active');
                for(var i = 0,len = $(this).parent().nextAll().length;i < len;i ++){
                    if($this.data('colId') === $(this).parent().nextAll().eq(i).data('itemId')){
                        $(this).parent().nextAll().eq(i).slideDown().addClass('rule-item-param-active');
                    }
                }

            }     

            /*选择数据*/
            if(optionName === 'result' || optionName === 'original'){

                /*获得要匹配的规则参数*/
                var $ruleParam =  $('#rule-data button.active').nextAll('.rule-item-param-active').find('.rule-item');

                /*重选数据已经选择过了*/
                if($(this).hasClass(optionName + '-col-active')){
                    $(this).removeClass(optionName + '-col-active');
                    /*匹配的参数重新匹配*/

                    for(var i = 0,len = $ruleParam.length;i < len;i ++){
                        if($(this).data('argumentSequence') === $ruleParam.eq(i).data('sequence')){
                            $ruleParam.eq(i).removeClass('rule-item-active').html($ruleParam.eq(i).data('description'));
                            $(this).removeData('argumentSquence');
                        }
                    }
                }else{
                    /*参数匹配*/
                    for(var i = 0,len = $ruleParam.length;i < len;i ++){
                        if( !$ruleParam.eq(i).hasClass('rule-item-active')){
                            if($ruleParam.eq(i).data('type') === $(this).data('type') || $ruleParam.eq(i).data('type') === '混合'){
                                $ruleParam.eq(i).addClass('rule-item-active').removeClass('rule-item-danger').html($(this).html());
                                $(this).addClass(optionName + '-col-active');
                                this.dataset.argumentSequence = $ruleParam.eq(i).data('sequence');
                            }else{
                                $ruleParam.eq(i).addClass('rule-item-danger');
                            }
                            break;
                        }
                    }
                }

            }    

            if(optionName === 'visual'){
                $this.toggleClass(optionName + '-col-active');
            }

            if($this.hasClass(optionName + '-col-active')){
                /*如果之前没有选择过*/

                if(selectedMsg[optionName + '_col'].indexOf( this.dataset.colId ) < 0){

                    if(optionName === 'result' || optionName === 'original'){
                        selectedMsg[optionName + '_col'].push( {
                            attribute_id:this.dataset.colId,
                            attribute_type:this.dataset.type,
                            attribute_sequence:this.dataset.sequence,
                            argument_sequence:this.dataset.argumentSequence
                        } );
                    }else if(optionName === 'rule'){
                        selectedMsg.rule = {
                            rule_id:$(this).data('colId'),
                            method_name:$(this).data('methodName')
                        };
                    }else{
                        selectedMsg[optionName + '_col'].push( this.dataset.colId );
                    }

                }

            }else{
                var newCol = [];
                $.each( selectedMsg[optionName + '_col'],function(key,value){

                    if( parseInt(value.attribute_id) != parseInt($this.data('colId'))){

                        newCol.push(value);
                        }
                    })
                selectedMsg[optionName + '_col'] = newCol;
            }
         
            console.log(selectedMsg);
            judSelectedAll();

        }
    })


    /*事件委托当鼠标移到（原始|结果）数据显示相应的数据选择*/
    $('.data-type').on('mouseover','li',function(){

        var $this = $(this),
        parent = this,
        name = this.className.substring(5),
        sibName = $this.siblings().not('.' + 'data-' + name).attr('class').substring(5);
        /*如果之前已经选择了*/
        if(selectedMsg[sibName + '_id'] !== ''){

            var oldSelectItem = $('#selsct-' + sibName + ' button.active');
            selectedMsg[sibName + '_id'] = '';
            selectedMsg[sibName + '_col'] = [];
        }

        while(parent.tagName.toUpperCase() !== 'UL'){
            parent = parent.parentNode;
        }

        /*如果之前已经添加过了*/
        if($('#select-' + name).not('hidden')){
            if($('.result-selected button').hasClass('active')){
                $('.result-selected button').removeClass('active');
            }
        }

        var selectAll = $(parent).nextAll('div').toArray();

        /*tab选项卡*/
        if(!selectedMsg[name + '_col'].length){
            for(var i = 0,len = selectAll.length;i < len;i ++){
                if(selectAll[i].id.indexOf(name) !== -1){
                    $(selectAll[i]).slideDown('fast');
                    $(this).find('a').css('color','black');
                }else{
                    $(selectAll[i]).slideUp('fast');
                    $('.data-' + sibName).find('a').css('color','#337ab7');
                    delectData();
                }
            }
        }


        var nowSelectItem = $('#' + name + '-data').find('.' + name + '-col-active').not(':hidden');


        if(nowSelectItem !== undefined && nowSelectItem){
            var newItem = [],
            btnActive = $('#' + name + '-data').find('.active').data('id');
            $.each(nowSelectItem,function(key,value){
                newItem.push($(this).data('col-id'));
            });
            selectedMsg[name + '_col'] = newItem;
            selectedMsg[name + '_id'] = btnActive === null ? '' : btnActive;
        }


        judSelectedAll();

    });

    /*点击数据表格形式展示*/
    $('.result-visual-selections').on('click','a',function(){

        var oParent = this;
        while(oParent.tagName.toUpperCase() !== 'DIV'){
            oParent = oParent.parentNode;
        }

        /*点击数据名称时*/
        if(oParent.id.indexOf('result') > -1 || oParent.id.indexOf('original') > -1){
            $.ajax({
                type:'get',
                url:'../servlet/VisualTableServlet',
                cache:'true',
                data:{
                    "flag":oParent.id.substring(7),
                    "id":$(this).siblings('button').data('id')
                },
                dataType:'json',
                success:function(res){
                    createTable(res.data);
                    $('#dataDesModal').modal('show');
                },
                error:function(){
                    alert('没有数据!');
                }
            });
        }
    });

    function createTable(dataArry){
        var table = document.createElement('table'),
            thead = document.createElement('thead'),
            tbody = document.createElement('tbody'),
            name = thead.insertRow(0);

        /*创建每一列的列名*/
        for(var i = 0,len = dataArry.length;i < len;i ++){  
            var itemName = name.insertCell(i);
            itemName.innerHTML = dataArry[i].label;
        }
        table.appendChild(thead);

        /*创建每一列的具体值*/
        for(var i = 0,len = dataArry[0].value.length;i < len;i ++){
            var itemData = tbody.insertRow(i);
            for(var j = 0,itemLen = dataArry.length;j < itemLen;j ++){
                itemData.insertCell(j).innerHTML = dataArry[j].value[i];
            }
        }
        table.appendChild(tbody);

        table.className = 'table table-hover table-bordered';

        var tableModal = $('.modal-body').get(2);
        tableModal.replaceChild(table,tableModal.firstChild);

        /*大致调整表格大小*/
        if(dataArry.length === 1){
            table.style.width = '50%';
        }else if(dataArry.length === 2){
            table.style.width = '75%';
        }else{
            table.style.width = '100%';
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
                data:[]//dataset.attribute_name[0]
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
});

