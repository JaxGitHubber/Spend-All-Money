package model;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

@Component
@Scope("prototype")
public class People {
	private List<Person> people;
	@Autowired
	private ApplicationContext context;
	
	public List<Person> getPeopleList() {
		return people;
	}
	
	public Person getPerson(String fullName) {
		for(Person person : people) {
			if(person.getFullName().equalsIgnoreCase(fullName)) {
				return person;
			}
		}
		return null;
	}
	
	@SuppressWarnings("serial")
	@PostConstruct
	private void fillPeopleList() {
		people = new ArrayList<Person>() {{
			add(new PersonBuilder().fullName("Pavel Durov").image("pavel-durov").state("15.500.000.000$").startLabel("TRY TO SPEND ALL PAVEL DUROV'S MONEY!").bio("Pavel Valerievich Durov (born October 10, 1984, Leningrad, Leningrad region, RSFSR, USSR) is a Russian entrepreneur and programmer, has citizenship of Saint Kitts and Nevis, UAE and France, dollar billionaire. One of the creators of the social network VKontakte and the company of the same name, the cross-platform messenger Telegram and other projects. Former general director of VKontakte (2006-2014). During his student years, he was a laureate of scholarships from the President of the Russian Federation and the Government of the Russian Federation, a three-time laureate of the Potanin Scholarship.").build(context));
			add(new PersonBuilder().fullName("Phil Knight").image("phil-knight").state("40.300.000.000$").startLabel("TRY TO SPEND ALL PHIL KNIGHT'S MONEY!").bio("Philip Hampson Knight (born February 24, 1938) is an American billionaire business magnate who is the co-founder and chairman emeritus of Nike, Inc., a global sports equipment and apparel company.").build(context));
			add(new PersonBuilder().fullName("Bill Gates").image("bill-gates").state("132.600.000.000$").startLabel("TRY TO SPEND ALL BILL GATES'S MONEY!").bio("William Henry Gates III (born October 28, 1955) is an American businessman, investor, philanthropist, and writer best known for co-founding the software company Microsoft with his childhood friend Paul Allen. During his career at Microsoft, Gates held the positions of chairman, chief executive officer (CEO), president, and chief software architect, while also being its largest individual shareholder until May 2014. He was a pioneer of the microcomputer revolution of the 1970s and 1980s.").build(context));
			add(new PersonBuilder().fullName("Mark Zuckerberg").image("mark-zuckerberg").state("173.500.000.000$").startLabel("TRY TO SPEND ALL MARK ZUCKERBERG'S MONEY!").bio("Mark Elliot Zuckerberg (born May 14, 1984) is an American businessman. He co-founded the social media service Facebook, along with his Harvard roommates in 2004, and its parent company Meta Platforms (formerly Facebook, Inc.), of which he is chairman, chief executive officer and controlling shareholder.").build(context));
			add(new PersonBuilder().fullName("Jeff Bezos").image("jeff-bezos").state("206.300.000.000$").startLabel("TRY TO SPEND ALL JEFF BEZOS'S MONEY!").bio("Jeffrey Preston Bezos (born January 12, 1964) is an American businessman, media proprietor and investor. He is the founder, executive chairman, and former president and CEO of Amazon, the world's largest e-commerce and cloud computing company.").build(context));
			add(new PersonBuilder().fullName("Elon Mask").image("elon-mask").state("210.200.000.000$").startLabel("TRY TO SPEND ALL ELON MASK'S MONEY!").bio("Elon Reeve Musk (born June 28, 1971) is a businessman and investor. He is the founder, CEO and Chief Engineer of SpaceX; investor, CEO and product architect of Tesla; founder of The Boring Company; co-founder of Neuralink and OpenAI; owner of X(Twitter).").build(context));
			
		}};
	}
}
