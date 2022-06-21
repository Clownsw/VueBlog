pub mod config;
pub mod controller;
pub mod dao;
pub mod pojo;
pub mod service;
pub mod util;

#[cfg(test)]
mod tests {
    use chrono::Duration;

    #[test]
    fn it_works() {
        let result = 2 + 2;
        assert_eq!(result, 4);
    }

    #[test]
    fn test_create_date() {
        println!(
            "{}",
            (chrono::Local::now() - Duration::days(1))
                .format("%Y-%m-%d")
                .to_string()
        );
    }
}
