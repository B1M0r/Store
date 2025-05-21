
import { useState, useEffect } from 'react';
import { useQuery } from '@tanstack/react-query';
import { fetchProducts } from '@/services/api';
import { Product } from '@/types';
import { Search, Plus } from 'lucide-react';
import ProductTable from '@/components/ProductTable';
import { Input } from '@/components/ui/input';
import { Button } from '@/components/ui/button';
import { Dialog, DialogContent, DialogHeader, DialogTitle } from '@/components/ui/dialog';
import ProductForm from '@/components/ProductForm';

const Index = () => {
  const [searchTerm, setSearchTerm] = useState<string>('');
  const [filteredProducts, setFilteredProducts] = useState<Product[]>([]);
  const [createDialogOpen, setCreateDialogOpen] = useState<boolean>(false);

  const { data: products = [], isLoading, error } = useQuery({
    queryKey: ['products'],
    queryFn: () => fetchProducts(),
  });

  useEffect(() => {
    if (products) {
      setFilteredProducts(
          products.filter(
              (product) =>
                  product.name.toLowerCase().includes(searchTerm.toLowerCase()) ||
                  product.category.toLowerCase().includes(searchTerm.toLowerCase())
          )
      );
    }
  }, [products, searchTerm]);

  return (
      <div className="space-y-6">
        <div className="flex flex-col md:flex-row gap-4 justify-between items-center max-w-2xl mx-auto">
          <div className="relative w-full">
            <Search className="absolute left-3 top-3 h-5 w-5 text-gray-400" />
            <Input
                type="text"
                placeholder="Поиск товаров..."
                value={searchTerm}
                onChange={(e) => setSearchTerm(e.target.value)}
                className="pl-10 h-12 text-lg rounded-full border-2 border-blue-600 focus-visible:ring-blue-600"
            />
          </div>
          <Button
              onClick={() => setCreateDialogOpen(true)}
              className="bg-blue-600 hover:bg-blue-700 text-white rounded-full h-12 px-6 flex-shrink-0"
          >
            <Plus className="mr-2 h-5 w-5" />
            Добавить товар
          </Button>
        </div>

        {isLoading ? (
            <div className="text-center p-10">
              <div className="inline-block h-8 w-8 animate-spin rounded-full border-4 border-solid border-blue-600 border-r-transparent"></div>
              <p className="mt-2">Загрузка товаров...</p>
            </div>
        ) : error ? (
            <div className="text-center text-red-500">Ошибка загрузки товаров</div>
        ) : (
            <ProductTable products={filteredProducts} />
        )}

        <Dialog open={createDialogOpen} onOpenChange={setCreateDialogOpen}>
          <DialogContent className="sm:max-w-md">
            <DialogHeader>
              <DialogTitle>Добавить новый товар</DialogTitle>
            </DialogHeader>
            <ProductForm onSuccess={() => setCreateDialogOpen(false)} />
          </DialogContent>
        </Dialog>
      </div>
  );
};

export default Index;