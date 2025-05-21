
import { useState } from 'react';
import { Product } from '@/types';
import { Table, TableBody, TableCell, TableHead, TableHeader, TableRow } from '@/components/ui/table';
import { Button } from '@/components/ui/button';
import { DropdownMenu, DropdownMenuContent, DropdownMenuItem, DropdownMenuTrigger } from '@/components/ui/dropdown-menu';
import { Dialog, DialogContent, DialogHeader, DialogTitle, DialogDescription, DialogFooter } from '@/components/ui/dialog';
import { Label } from '@/components/ui/label';
import { Input } from '@/components/ui/input';
import { toast } from '@/components/ui/use-toast';
import { updateProduct, deleteProduct } from '@/services/api';
import { MoreHorizontal, Edit, Trash2, Layers } from 'lucide-react';
import { useQueryClient } from '@tanstack/react-query';
import ProductDetails from './ProductDetails';

interface ProductTableProps {
  products: Product[];
}

const ProductTable = ({ products }: ProductTableProps) => {
  const queryClient = useQueryClient();
  const [editDialogOpen, setEditDialogOpen] = useState(false);
  const [deleteDialogOpen, setDeleteDialogOpen] = useState(false);
  const [detailsDialogOpen, setDetailsDialogOpen] = useState(false);
  const [currentProduct, setCurrentProduct] = useState<Product | null>(null);
  const [editForm, setEditForm] = useState<Partial<Product>>({});

  const handleEdit = (product: Product) => {
    setCurrentProduct(product);
    setEditForm({
      name: product.name,
      price: product.price,
      category: product.category,
    });
    setEditDialogOpen(true);
  };

  const handleDelete = (product: Product) => {
    setCurrentProduct(product);
    setDeleteDialogOpen(true);
  };

  const handleViewDetails = (product: Product) => {
    setCurrentProduct(product);
    setDetailsDialogOpen(true);
  };

  const handleEditSubmit = async () => {
    if (!currentProduct?.id) return;

    try {
      await updateProduct(currentProduct.id, {
        ...currentProduct,
        ...editForm,
      } as Product);

      queryClient.invalidateQueries({ queryKey: ['products'] });
      toast({
        title: "Продукт обновлён",
        description: `${editForm.name} был обновлён успешно.`,
      });
      setEditDialogOpen(false);
    } catch (error) {
      toast({
        title: "Ошибка",
        description: "Ошибка при обновлении продута.",
        variant: "destructive",
      });
    }
  };

  const handleDeleteSubmit = async () => {
    if (!currentProduct?.id) return;

    try {
      await deleteProduct(currentProduct.id);

      queryClient.invalidateQueries({ queryKey: ['products'] });
      toast({
        title: "Продукт удалён",
        description: `${currentProduct.name} был удалён успешно.`,
      });
      setDeleteDialogOpen(false);
    } catch (error) {
      toast({
        title: "Ошибка",
        description: "Не удалось удалить продукт.",
        variant: "destructive",
      });
    }
  };

  return (
      <>
        <div className="rounded-md border">
          <Table>
            <TableHeader>
              <TableRow>
                <TableHead>Название</TableHead>
                <TableHead>Категория</TableHead>
                <TableHead className="text-right">Цена</TableHead>
                <TableHead className="w-[150px]">Подробнее</TableHead>
                <TableHead className="w-[70px]"></TableHead>
              </TableRow>
            </TableHeader>
            <TableBody>
              {products.length === 0 ? (
                  <TableRow>
                    <TableCell colSpan={5} className="text-center py-6 text-muted-foreground">
                      No products found
                    </TableCell>
                  </TableRow>
              ) : (
                  products.map((product) => (
                      <TableRow key={product.id}>
                        <TableCell>{product.name}</TableCell>
                        <TableCell>{product.category}</TableCell>
                        <TableCell className="text-right">${product.price.toFixed(2)}</TableCell>
                        <TableCell>
                          <div className="flex space-x-1">
                            <Button
                                variant="outline"
                                size="sm"
                                className="flex items-center gap-1"
                                onClick={() => handleViewDetails(product)}
                            >
                              <Layers className="h-4 w-4" />
                              <span className="hidden md:inline"></span>
                            </Button>
                          </div>
                        </TableCell>
                        <TableCell>
                          <DropdownMenu>
                            <DropdownMenuTrigger asChild>
                              <Button variant="ghost" size="icon" className="h-8 w-8 p-0">
                                <MoreHorizontal className="h-4 w-4" />
                              </Button>
                            </DropdownMenuTrigger>
                            <DropdownMenuContent align="end">
                              <DropdownMenuItem onClick={() => handleEdit(product)}>
                                <Edit className="mr-2 h-4 w-4" />
                                Редактировать
                              </DropdownMenuItem>
                              <DropdownMenuItem onClick={() => handleDelete(product)} className="text-destructive">
                                <Trash2 className="mr-2 h-4 w-4" />
                                Удалить
                              </DropdownMenuItem>
                            </DropdownMenuContent>
                          </DropdownMenu>
                        </TableCell>
                      </TableRow>
                  ))
              )}
            </TableBody>
          </Table>
        </div>

        {/* Edit Dialog */}
        <Dialog open={editDialogOpen} onOpenChange={setEditDialogOpen}>
          <DialogContent>
            <DialogHeader>
              <DialogTitle>Редактировать продукт</DialogTitle>
              <DialogDescription>
                Внесите изменения для продукта.
              </DialogDescription>
            </DialogHeader>

            <div className="grid gap-4 py-4">
              <div className="grid grid-cols-4 items-center gap-4">
                <Label htmlFor="name" className="text-right">Name</Label>
                <Input
                    id="name"
                    value={editForm.name || ''}
                    onChange={(e) => setEditForm({ ...editForm, name: e.target.value })}
                    className="col-span-3"
                />
              </div>
              <div className="grid grid-cols-4 items-center gap-4">
                <Label htmlFor="category" className="text-right">Категория</Label>
                <Input
                    id="category"
                    value={editForm.category || ''}
                    onChange={(e) => setEditForm({ ...editForm, category: e.target.value })}
                    className="col-span-3"
                />
              </div>
              <div className="grid grid-cols-4 items-center gap-4">
                <Label htmlFor="price" className="text-right">Цена</Label>
                <Input
                    id="price"
                    type="number"
                    value={editForm.price || ''}
                    onChange={(e) => setEditForm({ ...editForm, price: Number(e.target.value) })}
                    className="col-span-3"
                />
              </div>
            </div>

            <DialogFooter>
              <Button variant="outline" onClick={() => setEditDialogOpen(false)}>Отмена</Button>
              <Button onClick={handleEditSubmit}>Сохранить изменения</Button>
            </DialogFooter>
          </DialogContent>
        </Dialog>

        {/* Delete Confirmation Dialog */}
        <Dialog open={deleteDialogOpen} onOpenChange={setDeleteDialogOpen}>
          <DialogContent>
            <DialogHeader>
              <DialogTitle>Подтвердите удаление</DialogTitle>
              <DialogDescription>
                Вы уверены, что хотите удалить "{currentProduct?.name}"? Это действие не может быть отменено.
              </DialogDescription>
            </DialogHeader>

            <DialogFooter>
              <Button variant="outline" onClick={() => setDeleteDialogOpen(false)}>Отмена</Button>
              <Button variant="destructive" onClick={handleDeleteSubmit}>Удалить</Button>
            </DialogFooter>
          </DialogContent>
        </Dialog>

        {/* Product Details Dialog with Relations */}
        {currentProduct && (
            <ProductDetails
                product={currentProduct}
                open={detailsDialogOpen}
                onClose={() => setDetailsDialogOpen(false)}
            />
        )}
      </>
  );
};

export default ProductTable;
