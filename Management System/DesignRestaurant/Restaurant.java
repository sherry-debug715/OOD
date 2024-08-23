package Management System.DesignRestaurant;

class NoTableException extends Exception{

	public NoTableException(Party p)
	{
		super("No table available for party size: " + p.getSize());
	}
}

class Meal {
	private float price;
	
	public Meal(float price) {
		this.price = price;
	}
	
	public float getPrice() {
		return this.price;
	}
}

class Order {
	private List<Meal> meals;
    // private Table table; // table the order belongs to
	
	public Order() {
		meals = new ArrayList<Meal>();
        // table = _table;
	}
	
	public List<Meal> getMeals() {
		return meals;
	}

    // public Table getTable() {
    //     return table;
    // }
	
	public void mergeOrder(Order order) {
		if(order != null) {
			for(Meal meal : order.getMeals()){
				meals.add(meal);
			}
		}
	}
	
	public float checkout() {
		int bill = 0;
		for(Meal meal : meals) {
			bill += meal.getPrice();
		}
		return bill;
	}
}

class Party {
	private int size;
	
	public Party(int _size) {
		size = _size;
	}
	
	public int getSize() {
		return size;
	}
}

class Table implements Comparable<Table>{
	private boolean isOccupied;
	private int capacity;
	private Order order;
	
	public Table(int _capacity) {
		capacity = _capacity;
		isOccupied = false;
		order = null;
	}
	
	public int getCapacity() {
		return capacity;
	}
	
	public boolean getIsOccupied() {
		return isOccupied;
	}
	
	public Order getCurrentOrder() {
		return order;
	}

	public void setOccupied() {
		isOccupied = true;
	}
	
	public void setNotOccupied() {
		isOccupied = false;
	}
	
	public void setOrder(Order newOrder) {
        if (newOrder == null) {
            order = null;
            return;
        }
		if(order == null) {
			order = newOrder;
		} else {
            order.mergeOrder(newOrder);
        }
	}

	@Override
	public int compareTo(Table compareTable) {
		return this.capacity - compareTable.getCapacity();
	}
}

public class Restaurant {
	private List<Table> tables;
	private List<Meal> menu;
	
	public Restaurant() {
        tables = new ArrayList<>();
        menu = new ArrayList<>();
	}
	
	public List<Meal> getMenu() {
		return menu;
	}

	public void addTable(Table table) {
        tables.add(table);
        Collections.sort(tables);
	}

	public void findTable(Party p) throws NoTableException {
        for (Table t : tables) {
            if (t.getCapacity() >= p.getSize() && !t.getIsOccupied()) {
                t.setOccupied();
                return;
            }
        }
        throw new NoTableException(p);
	}
	
	public void takeOrder(Table table, Order order) {
        table.setOrder(order);
	}
	
	public float checkOut(Table t) {
        float price = 0;
        if (t.getCurrentOrder() != null) {
            price = t.getCurrentOrder().checkout();
        }
        t.setOrder(null);
        t.setNotOccupied();
        return price;
	}
	
	
	
	public String restaurantDescription() {
		String description = "";
		for(int i = 0; i < tables.size(); i++) {
			Table table = tables.get(i);
			description += ("Table: " + i + ", table size: " + table.getCapacity() + ", isAvailable: " + !table.getIsOccupied() + ".");
			if(table.getCurrentOrder() == null)
				description += " No current order for this table"; 
			else
				description +=  " Order price: " + table.getCurrentOrder().checkout();
			
			description += ".\n";
		}
		description += "*****************************************\n";
		return description;
	}
}