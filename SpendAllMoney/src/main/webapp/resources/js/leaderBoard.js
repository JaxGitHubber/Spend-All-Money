
let currentPlayerTopNumber = Number(document.getElementById('your_top_number').innerHTML.replaceAll(".", ""));
if(currentPlayerTopNumber > 0 && currentPlayerTopNumber <= 30) {
	document.getElementById('indentation').style.display = 'none';
	document.getElementById('your_top').style.visibility = 'hidden';
}