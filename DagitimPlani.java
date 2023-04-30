import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public  class DagitimPlani {

    public static void main(String[] args) {
        // Verilen 10 noktanın koordinatları
        List<Point> locations = Arrays.asList(
                new Point(41.0082, 28.9784),
                new Point(41.0128, 28.9497),
                new Point(41.0025, 28.9739),
                new Point(41.0172, 28.9783),
                new Point(41.0082, 28.9984),
                new Point(41.0082, 28.9884),
                new Point(41.0082, 28.9684),
                new Point(41.0082, 28.9584),
                new Point(41.0082, 28.9384),
                new Point(41.0082, 28.9184)
        );

        // İhtiyaç listesi: Sağlık Malzemesi, Temel Gıda, Isınma Gereci, Giyecek
        List<Demand> demands = Arrays.asList(
                new Demand("Sağlık Malzemesi", 100),
                new Demand("Temel Gıda", 100),
                new Demand("Isınma Gereci", 70),
                new Demand("Giyecek", 70)
        );

        // Rota oluşturma
        List<Point> route = createRoute(locations);

        // İhtiyaçların dağıtım planı oluşturma
        DistributionPlanGenerator generator = new DistributionPlanGenerator(route, demands);
        List<DistributionPlanGenerator.Distribution> distributionPlan = generator.generate();

        // Sonuçları yazdırma
        for (DistributionPlanGenerator.Distribution distribution : distributionPlan) {
            System.out.println(distribution.demandType + " ihtiyacı " + distribution.amount + " adet " + distribution.location.toString() + " noktasına dağıtılacak.");
        }
    }

    // En kısa rota oluşturma
    private static List<Point> createRoute(List<Point> locations) {
        // OpenStreetMap API veya diğer yol tarifi servisleri kullanılarak en kısa rota oluşturulabilir.
        // Bu örnek için rota, yerlerin sırayla ziyaret edilmesi ile oluşturulacaktır.
        return locations;
    }

    // Koordinatları tutan Point sınıfı
    static class Point {
        double latitude;
        double longitude;

        Point(double latitude, double longitude) {
            this.latitude = latitude;
            this.longitude = longitude;
        }

        public String toString() {
            return "(" + this.latitude + ", " + this.longitude + ")";
        }
    }

    // İhtiyaçları tutan Demand sını
    static class Demand {
        String type;
        int amount;

        Demand(String type, int amount) {
            this.type = type;
            this.amount = amount;
        }
    }

    // Dağıtım planını oluşturan DistributionPlanGenerator sınıfı
    static class DistributionPlanGenerator {
        List<Point> route;
        List<Demand> demands;

        DistributionPlanGenerator(List<Point> route, List<Demand> demands) {
            this.route = route;
            this.demands = demands;
        }

        // Dağıtım planını oluşturma metodu
        public List<Distribution> generate() {
            List<Distribution> distributionPlan = new ArrayList<>();
            int demandIndex = 0;

            // Her bir ihtiyaç için dağıtım planı oluşturma
            for (Demand demand : demands) {
                int remainingAmount = demand.amount;

                // En kısa rota boyunca her bir lokasyona giderek ihtiyacın dağıtımını yapma
                for (int i = 0; i < route.size() && remainingAmount > 0; i++) {
                    Point location = route.get(i);
                    int amount = Math.min(remainingAmount, getStock(demand.type));

                    distributionPlan.add(new Distribution(demand.type, amount, location));
                    remainingAmount -= amount;
                    reduceStock(demand.type, amount);
                }

                if (remainingAmount > 0) {
                    System.out.println(demand.type + " ihtiyacı karşılanamadı.");
                }

                demandIndex++;
            }

            return distributionPlan;
        }

        // İhtiyacın tipine göre mevcut stok miktarını getiren metod
        private int getStock(String type) {
            switch (type) {
                case "Sağlık Malzemesi":
                    return 100;
                case "Temel Gıda":
                    return 100;
                case "Isınma Gereci":
                    return 70;
                case "Giyecek":
                    return 70;
                default:
                    return 0;
            }
        }

        // İhtiyacın tipine göre stok miktarını azaltan metod
        private void reduceStock(String type, int amount) {
            // Bu örnek için stoklar sabit olduğu için bir şey yapmıyoruz.
            // Gerçek bir senaryoda stok azaltılmalıdır.
        }

        // Dağıtım planı için kullanılan Distribution sınıfı
        static class Distribution {
            String demandType;
            int amount;
            Point location;

            Distribution(String demandType, int amount, Point location) {
                this.demandType = demandType;
                this.amount = amount;
                this.location = location;
            }
        }
    }
}