/**
 * 
 */
 
function search(page) {
	var target = $("#contentWrap");
	var keyword = $("#keyword").val();
	var tr;
	var clasificarArr = [];
	var subclassArr = [];
	$.each($('.clasificar_check'),function(index) {
		if($(this).is(':checked')) {
			//this.checked = true;
			clasificarArr.push($(this).val());
		}
	})
	
	$.each($('.subclass_check'),function(index) {
		if($(this).is(':checked')) {
			//this.checked = true;
			subclassArr.push($(this).val());
		}
	})
	
	if(page == 1 || page == null) {
		page = 1
	}

	$.ajax({
		url: "/search",
		type: "POST",
		traditional: true,
		data: {
			"clasificarArr" : clasificarArr,
			"subclassArr" : subclassArr,
			"keyword" : keyword,
			"page" : page,
			//"check" : JSON.stringify(checkArr)
		},
		dataType: "json",
		success: function(data) {
			target.empty();
			$.each(data.collect, function(key) {
				let data1 = data["collect"][key];
				let hi = data["highlight"][key];
				
				tr = "<div class='card' style='width: 50%; margin: 1rem auto;'>";
				tr += "<div class='card-body'>" + 
					"<div class='d-flex align-items-center justify-content-start'>" + 
					"<span>" + data1.formatPublished + "</span> <span>"+data1.doc_class_class+"</span>" +
					"<span>"+data1.doc_class_code+"</span>" + 
					"</div>";	
				if(hi.doc_title === undefined) {
					tr += "<h4 class='card-title'>"+data1.doc_title+"</h4>";				
				} else {
					tr += "<h4 class='card-title'>"+hi.doc_title+"</h4>";
				}
				if(hi.context === undefined) {
					tr += "<p class='content' style='height: 60px; overflow: hidden; text-overflow: ellipsis; white-space: nowrap;'>"+data1.context+"</p>";
				} else {
					tr += "<p class='content' style='height: 60px; overflow: hidden; text-overflow: ellipsis; white-space: nowrap;'>"+hi.context+"</p>";
				}
				tr += "</div>";
				tr += "</div>";
				target.append(tr);
		 })
		printFooter(data.page);
		printCodeAggs(data.code, subclassArr);
		printClassAggs(data.cla, clasificarArr);

		},
		error: function(data) {
			console.log(data);
		}
	})
};

function printFooter(data) {
	let li = "";
	target = $("#footer");
	target.empty();
	if(data.page > 1) {
		li += "<li class='page-item' onclick='search(1)'><a class='page-link'> &lt;&lt; </a></li>";
		li += "<li class='page-item' onclick='search("+(data.page - 1)+")'><a class='page-link'> &lt; </a></li>";
	} 
	for(var i = data.start; i <= data.end; i++) {
		if(data.page == i) {
			li += "<li class='page-item active' onclick='search("+i+")'><a class='page-link'>"+i+"</a></li>";			
		} else {
			li += "<li class='page-item' onclick='search("+i+")'><a class='page-link'>"+i+"</a></li>";			
		}
	}
	
	 if(data.page < data.max) {
			var pageCalc = (data.start + data.page_SIZE ) > data.max ? data.max : (data.start + data.page_SIZE );
			li += "<li class='page-item' onclick='search("+(data.page + 1)+")'><a class='page-link'>&gt;</a></li>";
			li += "<li class='page-item' onclick='search("+data.max+")'><a class='page-link'>&gt;&gt;</a></li>";
	} 
	target.append(li);
							 
}

function printClassAggs(data, arr) {
	let target = $("#clasificar");
	target.empty();
	let tag = "";
	let targetHead = $("#clasificar_head");
	targetHead.empty();
	if(data.length > 0) {
		tag = "<tr><th colspan='3'>대분류</th></tr>";
		targetHead.append(tag);
	}
	$.each(data, function(key) {
		let code = data[key];
		tag = "<tr>";
		if(arr.includes(code.key)) {
			tag += "<td><input class='clasificar_check' type='checkbox' onclick='search(1)' value="+code.key+" checked></td>";
		} else {
			tag += "<td><input class='clasificar_check' type='checkbox' onclick='search(1)' value="+code.key+"></td>";			
		}
  		tag += "<td>"+code.key+"</td>";
  		tag += "<td>"+code.count+"</td>";
    	tag += "</tr>";
    	target.append(tag);
    	
	})
}

function printCodeAggs(data, arr) {
	let target = $("#subclass");
	target.empty();
	let tag = "";
	let targetHead = $("#subclass_head");
	targetHead.empty();
	if(data.length > 0) {
		tag = "<tr><th colspan='3'>소분류</th></tr>";
		targetHead.append(tag);
	}
	$.each(data, function(key) {
		let code = data[key];
		tag = "<tr>";
		if(arr.includes(code.key)) {
			tag += "<td><input class='subclass_check' type='checkbox' onclick='search(1)' value="+code.key+" checked></td>";
		} else {
			tag += "<td><input class='subclass_check' type='checkbox' onclick='search(1)' value="+code.key+"></td>";
		}
  		tag += "<td>"+code.key+"</td>";
  		tag += "<td>"+code.count+"</td>";
    	tag += "</tr>";
    	target.append(tag);
	})

}
