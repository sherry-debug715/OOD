// enum type for Vehicle
enum VehicleSize {
    Motorcycle,
    Compact,
    Large,
}

abstract class Vehicle {
    protected VehicleSize size;
	protected String licensePlate;  // id for a vehicle

    public VehicleSize getVehicleSize() {
        return size;
    }

    public String getLicensePlate() {
        return licensePlate;
    }
}

class Motorcycle extends Vehicle {
    public Motorcycle() {
        size = VehicleSize.Motorcycle;
    }
}

class Car extends Vehicle {
    public Car() {
        size = VehicleSize.Compact;
    }
}

class Bus extends Vehicle {
    public Bus() {
        size = VehicleSize.Large;
    }
}

class ParkingLot {
    private int numOfLevels;
    private int numOfRows;
    private int numSpotsPerRow;
    private Level[] levels;
    private Map<String, ConsecutiveSpot> parkingMap;

    public ParkingLot(int _numOfLevels, int _numOfRows, int _numSpotsPerRow) {
        numOfLevels = _numOfLevels;
        numOfRows = _numOfRows;
        numSpotsPerRow = _numSpotsPerRow;
        levels = new Level[numOfLevels];
        for (int i = 0; i < numOfLevels; i++) {
            levels[i] = new Level(numOfRows, numSpotsPerRow);
        }
        parkingMap = new HashMap<>();
    }

    public int getNumOfLevels() {
        return numOfLevels;
    }

    public int getNumOfRows() {
        return numOfRows;
    }

    public int getNumSpotsPerRow() {
        return numSpotsPerRow;
    }

    public Level[] getLevels() {
        return levels;
    }

    public int getAvailableSpots() {
        int sum = 0;
        for (Level l : levels) {
            sum += l.getAvailableSpots();
        }
        return sum;
    }

    public Map<String, ConsecutiveSpot> getParkingMap() {
        return parkingMap;
    }

    public void setParkingMap(String licensePlate, ConsecutiveSpot cs, boolean isParking) {
        if (isParking) {
            parkingMap.put(licensePlate, cs);
        } else {
            parkingMap.remove(licensePlate);
        }
    }

    private ConsecutiveSpot findConsecutiveSpot(Vehicle vehicle) {
        // 1. check the size of vehicle, and get the start parking index 
        VehicleSize vehicleSize = vehicle.getVehicleSize();
        int length = 1;
        if (vehicleSize == VehicleSize.Large) {
            length = 5;
        }

        Level parkingLevel = null;
        int[] parkingRowAndPos = {-1, -1};
        for (int i = 0; i < levels.length; i++) {
            Level l = levels[i];
            parkingRowAndPos = l.findConsecutiveSpot(length, vehicleSize);
            if (parkingRowAndPos[0] != -1 && parkingRowAndPos[1] != -1) {
                parkingLevel = l;
                break;
            }
        }
        if (parkingRowAndPos[0] == -1 && parkingRowAndPos[1] == -1) {
            return null;
        }
        return new ConsecutiveSpot(parkingLevel, parkingRowAndPos[0], parkingRowAndPos[1], length);
    }

    // Park the vehicle in a spot (or multiple spots)
    // Return false if failed
    public boolean parkVehicle(Vehicle vehicle) {
        ConsecutiveSpot spotInfo = findConsecutiveSpot(vehicle);
        if (spotInfo == null) {
            return false;
        }
        // take the spot from the level and row 
        Level level = spotInfo.getLevel();
        int rowNum = spotInfo.getRow();
        Row parkedRow = level.getRows()[rowNum];
        parkedRow.setOccupiedSpots(spotInfo.getStartPos(), spotInfo.getLength(), true);
        // store where vehicle parked in the parkingMap;
        setParkingMap(vehicle.licensePlate, spotInfo, true);
        return true;
    }

    // unPark the vehicle
    public void unParkVehicle(Vehicle vehicle) {
        // get consecutiveSpot if the vehicle from parkingMap 
        Map<String, ConsecutiveSpot> parkingMap = getParkingMap();
        if (!parkingMap.containsKey(vehicle.licensePlate)) {
            return;
        }
        ConsecutiveSpot parkInfo = parkingMap.get(vehicle.licensePlate);
        // find the row the vehicle parked on
        Level level = parkInfo.getLevel();
        int rowIdx = parkInfo.getRow();
        Row row = level.getRows()[rowIdx];
        // unpark the vehicle 
        int startPos = parkInfo.getStartPos();
        int length = parkInfo.getLength();
        // Unpark the vehicle, making spots available
        row.setOccupiedSpots(startPos, length, false);
        // remove the vehicle from parkingMap
        setParkingMap(vehicle.licensePlate, null, false);
    }
}

class ConsecutiveSpot {
    private Level level;
    private int row;
    private int startPos;
    private int length;
    public ConsecutiveSpot(Level _level, int _row, int _startPos, int _length) {
        level = _level;
        row = _row;
        startPos = _startPos;
        length = _length;
    }

    public Level getLevel() {
        return level;
    }
    
    public int getRow() {
        return row;
    }

    public int getStartPos() {
        return startPos;
    }

    public int getLength() {
        return length;
    }
}

/* Represents a level in a parking garage */
class Level {
    private int numRows;
    private int numSpotsPerRow;
    private Row[] rows;
    public Level(int _numRows, int _numSpotsPerRow) {
        numRows = _numRows;
        numSpotsPerRow = _numSpotsPerRow;
        rows = new Row[numRows];
        for (int i = 0; i < numRows; i++) {
            rows[i] = new Row(numSpotsPerRow);
        }
    }

    public int getNumRows() {
        return numRows;
    }

    public int getNumSpotsPerRow() {
        return numSpotsPerRow;
    }

    public Row[] getRows() {
        return rows;
    }

    public int getAvailableSpots() {
        int sum = 0;
        for (Row r : rows) {
            sum += r.getAvailableSpots();
        }
        return sum;
    }

    public int[] findConsecutiveSpot(int length, VehicleSize size ) {
        int[] rowAndPos = new int[]{-1,-1}; // row, startPos
        for (int i = 0; i < rows.length; i++) {
            Row eachRow = rows[i];
            int startPos = eachRow.findConsecutiveSpot(length, size);
            if (startPos != -1) {
                rowAndPos[0] = i;
                rowAndPos[1] = startPos;
                break;
            }
        }
        return rowAndPos;
    }
}

class Row {
    private int numSpotsPerRow;
    private boolean[] occupiedSpots;
    private int availableSpots;
    public Row(int _numSpotsPerRow) {
        numSpotsPerRow = _numSpotsPerRow;
        occupiedSpots = new boolean[numSpotsPerRow];
        availableSpots = numSpotsPerRow;
    }

    public int getNumSpotsPerRow() {
        return numSpotsPerRow;
    }

    public boolean[] getOccupiedSpots() {
        return occupiedSpots;
    }

    public int getAvailableSpots() {
        return availableSpots;
    }

    public void setOccupiedSpots(int startIdx, int length, boolean isParking) {
        if (isParking) {
            for (int i = startIdx; i < startIdx + length; i++) {
                occupiedSpots[i] = true;
                availableSpots -= 1;
            }
        } else {
            for (int i = startIdx; i < startIdx + length; i++) {
                occupiedSpots[i] = false;
                availableSpots += 1;
            }
        }
    }

    public int findConsecutiveSpot(int length, VehicleSize size) {
        if (availableSpots < length) {
            return -1;
        }
        if (size == VehicleSize.Motorcycle) { // motorcycle can park in any spot
            for (int i = 0; i < numSpotsPerRow; i++) {
                if (!occupiedSpots[i]) {
                    return i;
                }
            }
        } else if (size == VehicleSize.Compact) { // A car park in single compact spot or large spot
            for (int i = numSpotsPerRow / 4; i < numSpotsPerRow; i++) {
                if (!occupiedSpots[i]) {
                    return i;
                }
            }
        } else if (size == VehicleSize.Large){ // A bus can park in five large spots that are consecutive 
            for (int i = numSpotsPerRow / 4 * 3; i <= numSpotsPerRow - length; i++) {
                boolean canPark = true;
                for (int j = 0; j < length; j++) {
                    if (occupiedSpots[i + j]) {
                        canPark = false;
                        i += j;
                        break;
                    }
                }
                if (canPark) {
                    return i;
                }
            }
        }
        return -1;
    }
}