    function start() {  
        
        window['progress'] = setInterval(function() {  
            var pbClient = PF('pbClient');  
              
            updateProgressBar();  
  
            if(pbClient.getValue() === 100) {  
                clearInterval(window['progress']);
                onComplete();
            }  
  
        }, 1000);  
    }  
  
    function cancel() {  
        clearInterval(window['progress']);  
        PF('pbClient').setValue(0);  
    }  
    
    
    function initDragDrop() {
    	
    	var leafs = $('.ui-treenode-leaf');
    	leafs.each(function(index){
    		$(this).children().first().draggable({
	           helper: function () {
	        	   var tmp = $(this).clone();
	        	   
	        	   $(this).find('.ui-treenode-label').attr('dd-type', 'leaf');
	        	   
	        	   tmp.appendTo('body').css('zIndex',1);
	        	   
	        	   tmp.children().first().remove();
	        	   tmp.addClass('wfdragitem');
	        	   tmp.find('.ui-treenode-icon').addClass('wfdragicon');
	        	   tmp.find('.ui-treenode-label').addClass('wfdraglabel');
	        	   
	        	   return tmp.show(); 
	           },
	           scope: 'treetotable',
	           zIndex: ++PrimeFaces.zindex
	        });
    	});
	
		var parents = $('.ui-treenode-parent');
		parents.each(function(index){
		    $(this).children().first().draggable({
		           helper: function () {
		        	   var tmp = $(this).clone();
		        	   
		        	   $(this).find('.ui-treenode-label').attr('dd-type', 'parent');
		        	   
		        	   tmp.appendTo('body').css('zIndex',1);
		        	   
		        	   tmp.children().first().remove();
		        	   tmp.addClass('wfdragitem');
		        	   tmp.find('.ui-treenode-icon').addClass('wfdragicon');
		        	   tmp.find('.ui-treenode-label').addClass('wfdraglabel');
		        	   
		        	   return tmp.show(); 
		           },
		           scope: 'treetotable',
		           zIndex: ++PrimeFaces.zindex
		        });
		});
	
		$('div.ui-layout-center div.ui-layout-unit-content').droppable({
           activeClass: 'wfdrop-active',
           hoverClass: 'wfdrop-highlight',
           tolerance: 'pointer',
           scope: 'treetotable',
           drop: function(event, ui) {
        	   var label = ui.draggable.find('.ui-treenode-label');
               var treeId = label.closest('li').attr('data-rowkey');
               
               treeToTable([
                    {name: 'property', value:  treeId}, {name: 'type', value: label.attr('dd-type')}
               ]);
           }
        });
		
	}
    