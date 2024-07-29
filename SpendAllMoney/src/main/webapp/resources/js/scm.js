function favorite(tag) {
    if(tag.alt == "false") {
        tag.src = "resources/images/heart.png";
        tag.alt = "true";
    } else {
        tag.src = "resources/images/love.png";
        tag.alt = "false";
    }
}

function deleteItem(tag) {
	fetch("/SpendAllMoney/whaleitem", {
		method: 'DELETE',
		body: tag.id
	});
	setTimeout(function() {
		window.location = "http://localhost:8081/SpendAllMoney/shoppingcart";
	}, 100);
}

function buy() {
	let money = decreasePrice();
	if(money >= 0) { 
		saveNewState(parseNumberToPrice("" + money) + ";" + parseNumberToPrice("" + (parsePriceToNumber(document.getElementById("fixedState").innerHTML) - money)));
		increaseTotalSpentMoneyForAccount();
	    updateHistory();
	    updateCountBarriers();
	    setTimeout(function() {
			deleteAll();
	     	window.location = "http://localhost:8081/SpendAllMoney/gratitude";
	    }, 100);
	} else {
		alert("not enough money to buy");
	}
}

function decreasePrice() {
    let price = parsePriceToNumber(document.getElementById("total_price").innerHTML);
    let parsedState = parsePriceToNumber(document.getElementById("state").innerHTML);
    return Number(parsedState) - Number(price);
}

function parseNumberToPrice(number) {
	if(number >= 0) {
        let numberWithpoints = "";
        let step = 1;
        for(let i = number.length-1; i >= 0; i--) {
          if(step == 3 && i-1 != -1) {
              step = 1;
              numberWithpoints = number.charAt(i) + numberWithpoints;
              numberWithpoints = "." + numberWithpoints;
          } else {
              numberWithpoints = number.charAt(i) + numberWithpoints;
              step++;
          }
        }
        return numberWithpoints + "$";
        }
}

function parsePriceToNumber(price) {
	return price.replace("$", "").replaceAll(".", "");
}

function saveNewState(state) {
	fetch("/SpendAllMoney/state", {
		method: 'POST',
		body: state
	});
}

function increaseTotalSpentMoneyForAccount() {
	if(document.getElementById("isSignIn").innerHTML == "SignIn") {
		fetch("/SpendAllMoney/leaderboard", {
			method: 'POST',
			body: document.getElementById("total_price").innerHTML
		});
	}
}

function updateHistory() {
	fetch("/SpendAllMoney/history", {
		method: 'POST'
	});
}

function updateCountBarriers() {
	fetch("/SpendAllMoney/items/barrier", {
		method: 'POST'
	});
}

function deleteAll() {
	fetch("/SpendAllMoney/items", {
		method: 'DELETE'
	});
}